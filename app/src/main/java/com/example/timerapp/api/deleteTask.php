<?php
include "connect.php";

// Lấy dữ liệu từ request
$task_id = isset($_POST['task_id']) ? intval($_POST['task_id']) : 0;

$response = [];

if ($task_id > 0) {
    $query = "DELETE FROM task WHERE id = $task_id";
    $data = mysqli_query($conn, $query);

    if ($data) {
        $arr = [
            "success" => true,
            "message" => "Xóa thành công"
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
        "message" => "Thiếu hoặc sai task_id"
    ];
}

echo json_encode($arr);
?>