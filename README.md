# 온도에 따른 옷 추천 웹사이트

- 거주지를 검색하여 그날의 기온에 입어야 할 옷을 추천해 주는 웹사이트
- MVC 모델을 사용.
- n시가 지나 n시 자료를 요청했으나, 자료가 없을 경우 n-1시 자료를 요청하여 출력.

## 추가해야하는 기능 :

- 거주지 데이터베이스 - 99%
- 거주지 Entity - 99%
- 거주지 DAO - 99%
- 기상 API - 99%
- (기상 API)요청받은 결과값 정리 - 80%
- JSP, JS, CSS - 99%
- 거주지 목록 AutoComplete - 99%
- 현재 위치 - 0%
- Controller - 99%

## Todo :

- 기상청에 요청을 보낼 때 타임아웃 될 경우, 에러 처리를 어떻게 할지.
- 작일 23시에 요청한 자료로 금일 최저, 최고 기온, 구름을 예측하기 때문에 정확도가 떨어짐.
- 현재 위치 기능 추가해야 함 - 이 기능이 어떻게 작동하느지에 따라 조회 부분도 다 고쳐야 할 수도 있음.

## Screenshot

![index](/Etc/index.png "index")
![list](/Etc/list.png "list")
![output](/Etc/output.png "output")
![output1](/Etc/output1.png "output1")
![error](/Etc/error.png "error")
![null](/Etc/null.png "null")