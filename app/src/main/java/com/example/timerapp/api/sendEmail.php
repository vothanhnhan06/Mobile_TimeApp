<?php
ini_set('display_errors', 1);
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);

include "connect.php";
use PHPMailer\PHPMailer\PHPMailer;
use PHPMailer\PHPMailer\Exception;
use PHPMailer\PHPMailer\SMTP;
require 'PHPMailer/src/Exception.php';
require 'PHPMailer/src/PHPMailer.php';
require 'PHPMailer/src/SMTP.php';

// Default response
$arr = [
    "success" => false,
    "error" => "Unknown error"
];

if (isset($_POST['email']) && isset($_POST['code'])) {
    $email = trim($_POST['email']);
    $code = trim($_POST['code']);

    // Kiểm tra email trống hoặc không hợp lệ
    if (empty($email)) {
        $arr = [
            "success" => false,
            "message" => "Email không được để trống!"
        ];
    } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
        $arr = [
            "success" => false,
            "message" => "Email không đúng định dạng!"
        ];
    } else {
        // Truy vấn kiểm tra email trong cơ sở dữ liệu
        $stmt = $conn->prepare("SELECT * FROM users WHERE email = ?");
        $stmt->bind_param("s", $email);
        $stmt->execute();
        $result = $stmt->get_result();
        $numrow = $result->num_rows;

        if ($numrow == 0) {
            $arr = [
                "success" => false,
                "message" => "Email không tồn tại trong hệ thống!"
            ];
        } else {
            // Gửi email OTP
            $mail = new PHPMailer(true);
            try {
                // Server settings
                $mail->SMTPDebug = 0;
                $mail->isSMTP();
                $mail->Host = 'smtp.gmail.com';
                $mail->SMTPAuth = true;
                $mail->Username = 'ten_mail_cua_ban';
                $mail->Password = 'ma_cua_ban';
                $mail->SMTPSecure = PHPMailer::ENCRYPTION_SMTPS;
                $mail->Port = 465;
                $mail->setFrom("ten_mail_cua_ban", 'TimerApp');
                $mail->addAddress($email);
                // Content
                $mail->isHTML(true);
                $mail->Subject = 'Verify CODE';
                $mail->Body = "Mã code của bạn là: $code. Mã code sẽ hết hạn trong vòng 3 phút!.";
                $mail->AltBody = 'This is the body in plain text for non-HTML mail clients';
                $mail->send();

                // Insert into Verify table
                $stmt1 = $conn->prepare("INSERT INTO otp_verification (email, otp, status) VALUES (?, ?, 'pending')");
                $stmt1->bind_param("ss", $email, $code);
                if ($stmt1->execute()) {
                    $arr = [
                        "success" => true,
                        "message" => "Đã gửi OTP!"
                    ];
                } else {
                    $arr = [
                        "success" => false,
                        "message" => "Không lưu được OTP!"
                    ];
                }
            } catch (Exception $e) {
                $arr = [
                    "success" => false,
                    "message" => "Gửi email không thành công!"
                ];
            }
        }
    }
} else {
    $arr = [
        "success" => false,
        "message" => "Không nhận được email va otp!"
    ];
}

// Always output JSON
header('Content-Type: application/json');
echo json_encode($arr);
?>