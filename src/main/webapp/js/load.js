  function load() {


    if ($('#local').val() === "") {

        $("#error").text('주소를 입력해주세요.')
        $("#error").show();
        $("#container").hide();
        $("#errorhp").hide();
        $('#loading').hide();

    } else {
        const local = $('#local').val();
        $.ajax({
            type: 'post',
            async: false,
            url: '/load',
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
                    $("#errorhp").hide();
                    //$("#time").text(jsonInfo.time)
                    $("#temp").text(jsonInfo.temp + "°C")
                    $("#info").text(jsonInfo.info)
                    $("#sky").text(jsonInfo.sky)
                    $("#wsd").text("(" + jsonInfo.wsd + "m/s)")
                    $("#reh").text(jsonInfo.reh + "%")
                    //$("#pty").text(jsonInfo.pty)
                    $("#baseTime").text(jsonInfo.baseTime)
                    $("#compass").text(jsonInfo.compass)
                    if(jsonInfo.pty === '0')
                        $("#pty").text('비나 눈이 내리지 않습니다.')
                    if(jsonInfo.pty === '1')
                        $("#pty").text('비가 내리는 중입니다.')
                    if(jsonInfo.pty === '2')
                        $("#pty").text('비와 눈이 내리는 중입니다.')
                    if(jsonInfo.pty === '3')
                        $("#pty").text('비눈이 내리는 중입니다.')
                    if(jsonInfo.pty === '5')
                        $("#pty").text('약간의 빗방울만 떨어지고 있습니다.')
                    if(jsonInfo.pty === '6')
                        $("#pty").text('약간의 빗방울 떨어지고 눈날림이 있습니다.')
                    if(jsonInfo.pty === '7')
                        $("#pty").text('약간의 눈날림이 있습니다.')

                    if(jsonInfo.errorcheck === '1'){
                        $("#errorhour").text(jsonInfo.errorhour)
                        $("#errorhp").show();
                        }
                }


                $('#loading').hide();
            }
        })
    }
}