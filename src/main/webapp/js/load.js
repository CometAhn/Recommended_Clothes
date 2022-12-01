$('#load').on("click", (e) => {

    if ($('#local').val() === "") {

        $("#error").text('주소를 입력해주세요.')
        $("#error").show();
        $("#container").hide();

    } else {
        const local = $('#local').val();
        $.ajax({
            type: 'post',
            async: false,
            url: 'http://localhost/load',
            dataType: 'text',
            data: { id: local },
            success: function (data, textStatus) {

                const jsonInfo = JSON.parse(data);

                if (jsonInfo.local === 'error') {
                    console.log('에러');
                    $("#error").text('오류가 발생했습니다. 다시 시도해주세요.')
                    $("#container").hide();
                    $("#error").show();
                } else {
                    console.log("도 : " + jsonInfo.city);
                    console.log("시/군/구 : " + jsonInfo.city_sub);
                    console.log("읍/면/동 : " + jsonInfo.local);
                    console.log("x값 : " + jsonInfo.position_x);
                    console.log("y값 : " + jsonInfo.position_y);
                    console.log("온도 : " + jsonInfo.temp + "도");
                    console.log("풍속 : " + jsonInfo.wsd + "m/s");
                    console.log("하늘상태 : " + jsonInfo.sky);
                    console.log("강수형태 : " + jsonInfo.pty);
                    console.log("옷 : " + jsonInfo.info);

                    $("#container").show();
                    $("#error").hide();
                    //$("#time").text(jsonInfo.time)
                    $("#temp").text(jsonInfo.temp + "°C")
                    $("#info").text(jsonInfo.info)
                    $("#sky").text(jsonInfo.sky)
                    $("#wsd").text(jsonInfo.wsd + "m/s")
                    $("#pty").text(jsonInfo.pty)
                }
            }
        })
    }
})