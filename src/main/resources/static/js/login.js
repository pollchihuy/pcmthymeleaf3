$(document).ready(function() {
        // Delegasi event untuk form login
        $(document).on('submit', '#form-loginz', function(e) {
            e.preventDefault();
            // alert('Enkrip dulu !');
            console.log('enkrip dulu !');
            var pwd = $('#password').val();// Paul@1234
            // var pwdEncrypt = btoa(pwd);// 0ija0sijer09u
            var regex =/^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[@_#\-$])[\w].{8,15}$/;
            var isOk = true;

            if(!regex.test(pwd)){
                isOk=false;
                document.getElementById("errorPwd").innerHTML = "Password nya error !!";
            }
            if(isOk){
                var pwdEncrypt = btoa(pwd);
                console.log('hasil enkrip !'+pwdEncrypt);
                $("#password").val(pwdEncrypt);
                // $("#form-loginz").submit();
                let post_url = $(this).attr("action"); //get form action url
                let request_method = $(this).attr("method"); //get form GET/POST method
                let form_data = $(this).serialize(); //Encode form elements for submission
                // let form_data = new Form(this); //Encode form elements for submission
                $.ajax({
                    url: post_url,
                    type: request_method,
                    data: form_data,
                })
                    .done(function(response) { //
                        let pattern = /My Login Page/i;
                        let result = response.match(pattern);
                        console.log('hasilnya adalah : '+result)
                        if(result)
                        {
                            $('html').html(response);
                        }
                        else
                        {
                            window.location = "/home";// ini refresh page nya
                        }
                    });
            };
        });
    });


