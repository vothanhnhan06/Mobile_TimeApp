<?php
include "connect.php";

// Đặt header để trả về JSON
header('Content-Type: application/json; charset=UTF-8');

// Kiểm tra kết nối cơ sở dữ liệu
if (!$conn) {
    $arr = [
        "success" => false,
        "message"=>"Kết nối không thành công!"
    ];
    echo json_encode($arr);
    exit();
}

// Lấy dữ liệu từ request
$email = isset($_POST['email']) ? $_POST['email'] : '';
$username = isset($_POST['username']) ? $_POST['username'] : '';
$pass = isset($_POST['password']) ? $_POST['password'] : '';
$old_pass = isset($_POST['old_password']) ? $_POST['old_password'] : '';


// Kiểm tra dữ liệu đầu vào
if (empty($email) || empty($username)) {
    $arr = [
        "success" => false
    ];
    
} else if((empty($pass) && !empty($old_pass))||(!empty($pass) && empty($old_pass))){
    $arr = [
        "success" => false,
        "message"=>"Nhập đủ thông tin"
    ];
} else if(empty($pass) && empty($old_pass)){
    $updateQuery = "UPDATE users SET name='$username' WHERE email = '$email'";
    $updateStmt = $conn->prepare($updateQuery);
    $updateStmt->execute();

    $arr = [
        "success" => true,
        "message" => "Cập nhật username thành công!"
    ];
}else{
// Check email đã tồn tại
$stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
$stmt->bind_param("s", $email);
$stmt->execute();
$result = $stmt->get_result();
$data = [];
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

if (!empty($data)) {
    $old_passs=$data[0];
    $str_old_pass = $old_passs['password'];

    if($str_old_pass!=$old_pass){
        $arr = [
        "success" => false,
        "message"=> "Mật khẩu cũ không khớp!"
    ];
    }else{
        if($str_old_pass==$pass){
            $arr = [
            "success" => false,
            "message"=> "Không dùng mật khẩu cũ!"
            ];
        }else{
            $updateQuery = "UPDATE users SET password = '$pass', name='$username' WHERE email = '$email'";
            $updateStmt = $conn->prepare($updateQuery);
            $updateStmt->execute();

            $arr = [
                "success" => true,
                "message" => "Cập nhật thành công!"
            ];
        }
        
    }
    
    
} else {
   $arr = [
        "success" => false,
        "message" => "Cập nhật không thành công!"
    ];
}
// Đóng kết nối
$stmt->close();
$conn->close();
}

// Trả về JSON
echo json_encode($arr);

?>