<?php
include "connect.php";
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
// Thiết lập múi giờ
date_default_timezone_set('Asia/Ho_Chi_Minh');

// Lấy dữ liệu từ request
$email = isset($_POST['email']) ? $_POST['email'] : '';
$otp = isset($_POST['code']) ? $_POST['code'] : '';

// Default response
$arr = [
    "success" => false,
    "message" => "Lỗi!"
];

// Kiểm tra nếu email hoặc otp không được cung cấp
if (empty($otp)) {
    $arr = [
        "success" => false,
        "message" => "Chưa nhập OTP!"
    ];
    exit;
}
if (empty($email)) {
    $arr = [
        "success" => false,
        "message" => "Chưa nhập email!"
    ];
    exit;
}

// Truy vấn để lấy OTP từ bảng otp_verification
$query = "SELECT * FROM otp_verification WHERE email = ? AND otp = ? AND status = 'pending'";
$stmt = $conn->prepare($query);
$stmt->bind_param("ss", $email, $otp);
$stmt->execute();
$result = $stmt->get_result();

$data = [];
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

// Kiểm tra nếu OTP tồn tại
if (!empty($data)) {
    $otpRecord = $data[0]; // Lấy bản ghi OTP đầu tiên

    // Lấy thời gian hiện tại
    $currentTime = time(); // 2025-05-16 23:14:00

    // Lấy thời gian tạo OTP
    $createdAt = strtotime($otpRecord['created_at']);
    if ($createdAt === false) {
        $arr = [
            "success" => false,
            "message" => "Invalid created_at timestamp"
        ];
        exit;
    }

    // Kiểm tra nếu có expires_at, nếu không thì tính toán
    if (isset($otpRecord['expires_at']) && !empty($otpRecord['expires_at'])) {
        $expiredAt = strtotime($otpRecord['expires_at']);
        if ($expiredAt === false) {
            $arr = [
                "success" => false,
                "message" => "Invalid expires_at timestamp"
            ];
            exit;
        }
    } else {
        // Nếu không có expires_at, tính toán: created_at + 3 phút (180 giây)
        $expiredAt = $createdAt + 180;
    }

    // Kiểm tra nếu OTP còn hiệu lực
    if ($currentTime >= $createdAt && $currentTime <= $expiredAt) {
        // OTP còn hiệu lực
        // Cập nhật trạng thái OTP thành 'used'
        $updateQuery = "UPDATE otp_verification SET status = 'used' WHERE email = ? AND otp = ?";
        $updateStmt = $conn->prepare($updateQuery);
        $updateStmt->bind_param("ss", $email, $otp);
        $updateStmt->execute();

        //Cập nhật trạng thái của user thành active trong trường hợp check OTP đăng kí
        $updateQuery1 = "UPDATE users SET status = 'active' WHERE email = ?";
        $updateStmt1 = $conn->prepare($updateQuery1);
        $updateStmt1->bind_param("s", $email);
        $updateStmt1->execute();

        //Lấy ID của user vừa được cập nhật
        $selectQuery = "SELECT user_id FROM users WHERE email = ?";
        $selectStmt = $conn->prepare($selectQuery);
        $selectStmt->bind_param("s", $email);
        $selectStmt->execute();
        $selectResult = $selectStmt->get_result();

        if ($selectRow = $selectResult->fetch_assoc()) {
            $user_id = $selectRow['user_id'];

            //Thêm thư mục mặc định cho user
            $defaultFolderName = "Thư mục mặc định"; // hoặc "Default Folder"
            $insertQuery = "INSERT INTO folder (user_id, name_folder) VALUES (?, ?)";
            $insertStmt = $conn->prepare($insertQuery);
            $insertStmt->bind_param("is", $user_id, $defaultFolderName);
            $insertStmt->execute();

            //Lấy ID của folder vừa được tạo
            $selectQuery1 = "SELECT id FROM folder WHERE user_id = ? AND name_folder=?";
            $selectStmt1 = $conn->prepare($selectQuery1);
            $selectStmt1->bind_param("is", $user_id,$defaultFolderName);
            $selectStmt1->execute();
            $selectResult1 = $selectStmt1->get_result();

            if ($selectRow1 = $selectResult1->fetch_assoc()) {
                $folder_id = $selectRow1['id'];

                //Thêm task vào thư mục mặc định
                $defaultTaskName = "Nhiệm vụ mặc định"; // hoặc "Default Task"
                $defaultTime = "01:00:00"; // kiểu TIME trong MySQL
                $isFavorite = 1;

                $insertQuery1 = "INSERT INTO task (title, id_user, `time`, is_favorite, id_folder) 
                                VALUES (?, ?, ?, ?, ?)";
                $insertStmt1 = $conn->prepare($insertQuery1);
                $insertStmt1->bind_param("sisii", $defaultTaskName, $user_id, $defaultTime, $isFavorite, $folder_id);
                $insertStmt1->execute();
            }
        }

        $arr = [
            "success" => true,
            "message" => "Xác nhận thành công!"
        ];
    } else {
        // OTP đã hết hạn
        $arr = [
            "success" => false,
            "message" => "Mã OTP hết hạn!",
        ];
    }
} else {
    // OTP không tồn tại hoặc đã được sử dụng
    $arr = [
        "success" => false,
        "message" => "OTP không tồn tại hoặc đã được sử dụng!"
    ];
}
header('Content-Type: application/json');
echo json_encode($arr);
?>