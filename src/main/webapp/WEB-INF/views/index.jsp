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
    <link href="http://localhost/css/main.css" rel="stylesheet">
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

    <div class="ui-widget">
      <label for="tags">읍/면/동 : </label>
      <input id="tags">
    </div>


    <div id="loading"><img id="loading-image" src="/images/loading.gif" alt="Loading..." /></div>


    <script>
      $( function() {
        var availableTags = [<c:forEach var="object" items="${val}">"${object}", </c:forEach>];
        $( "#tags" ).autocomplete({
          source: availableTags
        });
      } );
      </script>
</body>
</html>