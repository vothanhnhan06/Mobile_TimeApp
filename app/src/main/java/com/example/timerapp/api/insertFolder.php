<?php
// Include database connection
include "connect.php";

// Get POST data
$user_id = isset($_POST['user_id']) ? trim($_POST['user_id']) : null;
$folder_name = isset($_POST['folder_name']) ? trim($_POST['folder_name']) : null;

// Validate required fields
if ($user_id === null ||$folder_name === null ) {
    echo json_encode([
        "success" => false,
        "message" => "Thiếu dữ liệu bắt buộc",
        "result" => []
    ]);
    exit;
}

// Sanitize inputs and prepare SQL statement
$stmt = $conn->prepare("INSERT INTO folder (user_id, name_folder) VALUES (?, ?)");
// Bind parameters (s: string, i: integer)
$stmt->bind_param("ss", $user_id, $folder_name);

if ($stmt->execute()) {
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