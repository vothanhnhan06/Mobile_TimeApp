<?php
include "connect.php";

// Lấy dữ liệu từ request
$user_id = isset($_POST['user_id']) ? $_POST['user_id'] : '29';

$query="SELECT * FROM task WHERE id_user='$user_id'";
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
        "message"=>"Trống!"
    ];
}
print_r(json_encode($arr));
?>