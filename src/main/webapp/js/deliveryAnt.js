jQuery(document).ready(function(){

    $("#signupBtn").click(function(){        
        //var data = new FormData(document.getElementById("signupForm"));
        //console.log(data);
        var data = $("form").serialize();
    
        var data = '{"companyName":"'+$("#companyName").val()
        +'","email":"'+$("#email").val()
        +'","address":"'+$("#address").val()
        +'","phone":"'+$("#phone").val()
        +'","password":"'+$("#password").val()
        +'"}';
        
        console.log(JSON.parse(data));
        
        $.ajax({
            url:"/register",
            data:data,
            // cache:false,
            // processType:false,
            contentType:"application/json",
            type:'POST',
            success:function(response){
                console.log(response);
            }
        });

        // $.post("/register",JSON.stringify(data),function(response){
        //     console.log(response);
        // });
    });
});