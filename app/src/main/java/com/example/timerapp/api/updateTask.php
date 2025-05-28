<?php
include "connect.php";

// Lấy dữ liệu từ request
$task_id = isset($_POST['task_id']) ? intval($_POST['task_id']) : 0;
$time = isset($_POST['time']) ? $_POST['time'] : '';

$response = [];

if ($task_id > 0 && preg_match('/^\d{2}:\d{2}:\d{2}$/', $time)) {
    $query = "UPDATE task SET time='$time' WHERE id = $task_id";
    $data = mysqli_query($conn, $query);

    if ($data) {
        $arr = [
            "success" => true,
            "message" => "Cập nhật thành công"
        ];
    } else {
        $arr = [
            "success" => false,
            "message" => "Lỗi truy vấn: " . mysqli_error($conn)
        ];
    }
} else {
    $arr = [
        "success" => false,
        "message" => "Thất bại! Dữ liệu không hợp lệ."
    ];
}

echo json_encode($arr);
?>