<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="url shortener">
    <meta property="og:type" content="website">
    <meta property="og:title" content="단축 주소 생성">
    <meta property="og:url" content="ipi.pw">
    <meta property="og:description" content="단축 주소 생성 웹페이지입니다.">
    <meta property="og:image" content="test.png">
    <title>기온 별 옷 추천</title>
    <link href="http://localhost/css/main.css?test" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-3.6.0.js"></script>
    <script src="https://code.jquery.com/ui/1.13.2/jquery-ui.js"></script>
    <link rel="stylesheet" href="//code.jquery.com/ui/1.13.2/themes/base/jquery-ui.css">
    <script src="/js/loading.js"></script>
    <script type="text/javascript">
        function loading() {
            $('#loading').show();
        }
    </script>

</head>

<body>

    <div id="loading"><img id="loading-image" src="/images/loading.gif" alt="Loading..." /></div>

    <center>
        <h1>기온 별 옷차림</h1>
        <div class="title">
            <label for="local">시/군/구 읍/면/동</label>
        </div>
        <div class="title">
            <input id="local" type="text" class="input">
        </div>
        <div class="title">
            <input type="button" value="확인" id="load" class="submit">
        </div>

        <div id="container" style="display: none;">
            기준 시간 :
            <h4 id="time"></h4><br><br>
            현재 기온은
            <h4 id="temp"></h4> 입니다.<br><br>
            추천하는 옷차림은 '
            <h4 id="info"></h4>'입니다.<br><br>
            오늘 하늘은 '
            <h4 id="sky"></h4>'상태입니다.<br><br>
            풍속은 '<h4 id="wsd"></h4>'이고, 강수 형태는 '<h4 id="pty"></h4>'입니다.
        </div>


        <div id="error" style="display: none;">
            <p class="errorp">오류가 발생했습니다. 다시 시도해주세요.</p>
        </div>

        <script>
            $(function () {
                var availableTags = [<c:forEach var="object" items="${val}">"${object}", </c:forEach>];
                $("#local").autocomplete({
                    source: availableTags
                });
            });


            $('#load').on("click", (e) => {

                $('#loading').show();
                const local = $('#local').val();
                $.ajax({
                    type: 'post',
                    async: false,
                    url: 'http://localhost/load',
                    dataType: 'text',
                    data: { id: local },
                    success: function (data, textStatus) {

                        $('#loading').hide();
                        const jsonInfo = JSON.parse(data);

                        if (jsonInfo.local === 'error') {
                            console.log('에러');
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
            })
        </script>
</body>
</html>