<?php
include "connect.php";

// Lấy dữ liệu từ request
$task_id = isset($_POST['task_id']) ? intval($_POST['task_id']) : 0;
$is_favorite = isset($_POST['is_favorite']) ? intval($_POST['is_favorite']) : 0;

$response = [];

if ($task_id > 0) {
    $query = "UPDATE task SET is_favorite='$is_favorite' WHERE id = $task_id";
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
        "message" => "Thất bại!"
    ];
}

echo json_encode($arr);
?>