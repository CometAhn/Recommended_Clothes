<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="author" content="url shortener">
    <meta property="og:type" content="website">
    <meta property="og:title" content="옷">
    <meta property="og:url" content="ipi.pw">
    <meta property="og:description" content="기온 별 옷">
    <meta property="og:image" content="test.png">
    <title>기온 별 옷 추천</title>
    <link href="/css/main.css" rel="stylesheet">
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
    <h1>기온 별 옷차림</h1>
    <form onSubmit="return false;">
        <div class="title">
            <label for="local">시/군/구 읍/면/동</label>
        </div>
        <div class="title">
            <input id="local" type="text" class="input" autocomplete="off">
            <input type="submit" value="조회" id="load" class="submit">
        </div>
    </form>

    <div id="container" style="display: none;">
        <div id="errorhp">
            <h4 id="errorhour"></h4>시 데이터가 존재하지 않아 이전 데이터를 불러옵니다.<br><br><br>
        </div>
        <h4 id="baseTime"></h4> 기상 정보입니다.<br><br>
        오늘 최저 온도는 <h4 id="tmn"></h4>이고, 최고 온도는 <h4 id="tmx"></h4>입니다.<br><br>
        현재 기온은 <h4 id="temp"></h4> 입니다.<br><br>
        추천하는 옷차림은 '<h4 id="info"></h4>'입니다.<br><br>
        현재 하늘은 <h4 id="sky"></h4><br><br>
        현재 습도는 '<h4 id="reh"></h4>'입니다.<br><br>
        바람은 '<h4 id="compass"></h4><h4 id="wsd"></h4>'이고, <h4 id="pty"></h4><br>
        <p style="font-size:14px; color: red; margin:5px;">※ 최저/최고 온도 및 구름은 작일 23시 예측 자료이므로, 차이가 있을 수 있습니다.</p><br>
    </div>

    <div id="error" style="display: none;">
        <p class="errorp"></p>
    </div>

    <script>
        $(function () {
            var availableTags = [<c:forEach var="object" items="${val}">"${object}", </c:forEach>];
            $("#local").autocomplete({
                source: availableTags
            });
        });
        $('#load').click(function () {
            $('#loading').show();
            window.setTimeout(function () {
                load();
            }, 10);

        });
    </script>
    <script src="/js/load.js"></script>
</body>
</html>