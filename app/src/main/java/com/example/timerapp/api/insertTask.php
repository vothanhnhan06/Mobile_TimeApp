<?php
// Include database connection
include "connect.php";

// Get POST data
$user_id = isset($_POST['user_id']) ? trim($_POST['user_id']) : null;
$folder_id = isset($_POST['folder_id']) ? $_POST['folder_id'] : null;
$task_name = isset($_POST['task_name']) ? trim($_POST['task_name']) : null;
$timecount = isset($_POST['time']) ? trim($_POST['time']) : null;
$isFavorite = isset($_POST['isFavorite']) ? (int)$_POST['isFavorite'] : 0;

// Validate required fields
if ($user_id === null || $folder_id === null || $task_name === null || $timecount === null) {
    echo json_encode([
        "success" => false,
        "message" => "Thiếu dữ liệu bắt buộc",
        "result" => []
    ]);
    exit;
}

// Validate folder_id is numeric
if (!is_numeric($folder_id) || (int)$folder_id <= 0) {
    echo json_encode([
        "success" => false,
        "message" => "ID thư mục không hợp lệ",
        "result" => []
    ]);
    exit;
}

// Validate time format (HH:mm:ss)
if (!preg_match("/^([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]$/", $timecount)) {
    echo json_encode([
        "success" => false,
        "message" => "Định dạng thời gian không hợp lệ. Sử dụng HH:mm:ss",
        "result" => []
    ]);
    exit;
}

// Validate isFavorite is 0 or 1
if ($isFavorite !== 0 && $isFavorite !== 1) {
    echo json_encode([
        "success" => false,
        "message" => "Giá trị isFavorite phải là 0 hoặc 1",
        "result" => []
    ]);
    exit;
}

// Sanitize inputs and prepare SQL statement
$stmt = $conn->prepare("INSERT INTO task (id_user, id_folder, title, `time`, is_favorite) VALUES (?, ?, ?, ?, ?)");
if ($stmt === false) {
    echo json_encode([
        "success" => false,
        "message" => "Lỗi chuẩn bị truy vấn: " . $conn->error,
        "result" => []
    ]);
    exit;
}

// Bind parameters (s: string, i: integer)
$stmt->bind_param("sissi", $user_id, $folder_id, $task_name, $timecount, $isFavorite);

if ($stmt->execute()) {
    $task_id = $conn->insert_id; // Get the ID of the newly inserted task
    echo json_encode([
        "success" => true,
        "message" => "Thêm task thành công"
    ]);
} else {
    echo json_encode([
        "success" => false,
        "message" => "Thêm task thất bại: " . $stmt->error
    ]);
}

// Clean up
$stmt->close();
$conn->close();
?>