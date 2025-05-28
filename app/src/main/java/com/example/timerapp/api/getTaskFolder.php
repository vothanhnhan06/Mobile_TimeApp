<?php
include "connect.php";

// Lấy dữ liệu từ request
$id_folder = isset($_POST['id_folder']) ? $_POST['id_folder'] : '1';

$query="SELECT * FROM task WHERE id_folder='$id_folder'";
$data=mysqli_query($conn,$query);
$result=array();
while($row=mysqli_fetch_assoc($data)){
    $result[]=($row);

}
if(!empty($result)){
    $arr = [
        "success" => true,
        "result"=>$result
        ];
}else{
    $arr = [
        "success" => false,
        "message"=>"Trống"
    ];
}
print_r(json_encode($arr));
?>