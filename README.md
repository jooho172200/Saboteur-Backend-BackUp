# Saboteur Backend (Team Project Backup)

이 저장소는 실시간 카드 게임 Saboteur의 백엔드 구현을 위한 팀 프로젝트 코드를 백업한 것입니다(organization 삭제 대비).  
저는 이 프로젝트에서 백엔드 개발을 담당하였고, 핵심 로직과 구조 설계에 기여했습니다.

## 프로젝트 개요

- Java, Spring Boot 기반 실시간 멀티플레이 게임 서버
- WebSocket을 이용한 카드 사용 및 이벤트 처리
- 카드, 게임판, 사용자 도메인 설계 및 서비스 로직 구현

## 주요 기여 내용

- 도메인 설계: Card, User 등 핵심 구조 설계
- 카드 서비스: ActionCard, PathCard 사용 처리 로직 분리
- 실시간 처리: Socket.IO 기반 이벤트 흐름 설계 및 구현
- 테스트: 카드 효과 중심 단위 테스트 작성

## 기술 스택

- Java 17 / Spring Boot 3 / JPA / MySQL
- WebSocket (Socket.IO)
- Gradle, Git

## 동기화 방법

본 저장소는 원본 organization 리포지토리의 변경사항을 다음과 같이 반영합니다:

```bash
git remote add upstream https://github.com/Software-Engineering-GoldStone/Backend.git
git fetch upstream
git checkout develop
git merge upstream/develop
git push origin develop
