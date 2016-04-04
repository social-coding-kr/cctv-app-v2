# social-coding-cctv_android

1. 프로젝트에 대한 개략적인 설명.
  1) 하나의 액티비티의 다수의 프래그먼트가 존재하는 구조.
  2) 안드로이드의 UI 표현 구조상, 하나의 쓰레드에서 ui갱신이 이루어지고 데이터 저장, 가공, 추출 등은 다수의 백그라운드 스레드로 이루어지도록 작업 해야함.
  3) 개발 자체는 UI개발이 본격적으로 이루어지기 전에는 하나의 개발 페이지에서 단순 요청 및 응신에 대한 결과를 볼 수 있는 개발 프래그 먼트에서 작업을 진행한다.
  ( 해당 작업 페이지가 완료 되면, ui개발을 진행하여 통신 및 데이터 이슈를 최소화)
2. 프로젝트 담당 업무 총 3가지 파트로 나누도록 결정됨.
  1) UI 처리하는 UI 쓰레드  담당 - 메타스토어에서 데이터를 가져와 UI갱신하는 인터페이스 구조 확립.
  2) Restful Api 에서 데이터 송/수신하여 데이터를 전달/응답 결과를 받아오는 네트워크 부분.
  3) 상기 2)의 항목에서 공급되는 데이터를 제공/저장 하는 역할 및 ui 쓰레드에 데이터 갱신 여부를 알려줄 수 있는 이벤트 인터페이스 구조를 개발하는 부분.

개발 기타 사항 :
  구글 맵을 이용하는 키의 경우 각 머신 별로 할당하므로, 이에 따른 수정후 개발이 가능함을 염두할 것.
  - 구조
    AndroidManifasts.xml
```
<manifestxmlns:android="http://schemas.android.com/apk/res/android"
package="com.example.ringo.myapplication
">

<application


Youcanalsoaddyourcredentialstoanexistingkey,usingthisline:
BE:46:C9:40:B7:41:7E:7A:DD:08:BF:53:4C:FF:98:B3:7E:AB:F6:AD;com.example.ringo.myapplication

Alternatively,followthedirectionshere:
https://developers.google.com/maps/documentation/android/start#get-key

Onceyouhaveyourkey(itstartswith"AIza"),replacethe"google_maps_key"
stringinthisfile.
-->
<stringname="google_maps_key"templateMergeStrategy="preserve"translatable="false">YOUR_KEY_HERE</string>
</resources>
```
참조 URL
https://developers.google.com/maps/documentation/android-api/signup#android_api
