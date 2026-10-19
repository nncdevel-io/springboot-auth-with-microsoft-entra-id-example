---
title: Spring Boot 4.1・Java 25 へのバージョンアップ
priority: high
created: 2026-10-19
owner: t-izuno
related:
  - docs/TASK.md
---

# TASKS

マイルストーン: M1 Spring Boot 4.1・Java 25 へのバージョンアップ
ゴール: Spring Boot 4.1.1・Spring Cloud Azure 7.4.0・Java 25 で、ビルド・テスト・CI・Entra ID ログインが通る状態にする

## ワークフロールール

- タスク着手時にステータスを 🚧 に更新する
- 必要な検証をエージェント単独で完遂できた場合は ✅、人の確認が残る場合は 🧪 に更新し、未確認事項を明記する。判断がつかない場合は 🧪 に倒す
- 🧪 から ✅ への更新はユーザーのレビュー承認をもって行う
- DependsOn のタスクがすべて ✅ でないタスクには着手しない

## ステータス表記ルール

| Status | 意味 |
| ---- | ----- |
| ⏳ | 未着手、TODO |
| 🚧 | 作業中、IN_PROGRESS |
| 🧪 | 確認待ち、REVIEW |
| ✅ | 完了、DONE |
| 🚫 | 中止、CANCELLED |

## タスク一覧

| ID | Status | Summary | DependsOn |
| --- | --- | --- | --- |
| TASK-001 | ✅ | 作業ブランチを作成し、更新前のビルドとテストの結果を記録する | - |
| TASK-002 | ✅ | Maven Wrapper を Maven 3.9.16・Wrapper 3.3.4 に更新する | TASK-001 |
| TASK-003 | ✅ | pom.xml の Boot・Azure BOM・Java・HTTP クライアントの版を上げる | TASK-002 |
| TASK-004 | ✅ | pom.xml のスターターを Boot 4 の名称に変え、テスト用スターターを足す | TASK-003 |
| TASK-005 | ✅ | テスト3ファイルの AutoConfigureMockMvc の import を新パッケージに直す | TASK-004 |
| TASK-006 | ✅ | SecurityConfig を Spring Security 7 で動くよう直し、全テストを通す | TASK-005 |
| TASK-007 | 🧪 | 実テナントで Entra ID のログイン・プロフィール表示・ログアウトを確認する | TASK-006 |
| TASK-008 | ✅ | ci.yml の JDK を 25 に変え、各 Action を最新メジャー版に上げる | TASK-006 |
| TASK-009 | ✅ | README.md と SOFTWARE_ARCHITECTURE.md の版と依存一覧を更新する | TASK-006 |
| TASK-010 | ⏳ | 変更一式のプルリクエストを作成し、CI の成功を確認する | TASK-007,TASK-008,TASK-009 |

## タスク詳細（補足が必要な場合のみ）

### TASK-001

- 補足: 更新前（Spring Boot 3.5.14、JDK 25.0.4 で実行）の `./mvnw clean test` は13件成功、失敗0件、スキップ0件だった

### TASK-003

- 補足: 対象は spring-boot-starter-parent 4.1.1、spring-cloud-azure.version 7.4.0、java.version 25、azure-core-http-jdk-httpclient 1.2.0
- 注意: Spring Cloud Azure 7.4.0 の検証済み範囲は Spring Boot 4.0.0〜4.1.0 で、4.1.1 は未検証である
- 補足: azure-core-http-jdk-httpclient は版の指定を外し、Azure BOM が管理する 1.1.5 を使う。1.2.0 は azure-core 1.60.0 を前提とし、BOM の azure-core 1.58.1 と合わないため採用しない

### TASK-004

- 補足: spring-boot-starter-web は spring-boot-starter-webmvc に、spring-boot-starter-oauth2-client は spring-boot-starter-security-oauth2-client に変える
- 補足: 追加するのは spring-boot-starter-webmvc-test と spring-boot-starter-security-test。spring-boot-starter-security-oauth2-client-test はテストで必要になった場合だけ足す
- 注意: thymeleaf-extras-springsecurity6 は Spring Boot 4.1.1 でも管理対象のため変えない

### TASK-005

- 補足: 対象は SecurityConfigTests、HomeControllerTests、ProfileControllerTests。新しいパッケージは org.springframework.boot.webmvc.test.autoconfigure

### TASK-006

- 注意: AadWebApplicationHttpSecurityConfigurer は 7.4.0 にも同じパッケージで残っている。修正が要らない場合はビルドとテストの成功だけを記録する
- 補足: SecurityConfig は変更不要だった。`./mvnw clean test` は13件成功、コンパイル時の非推奨警告と実行時の WARN はなかった

### TASK-007

- 注意: Entra ID テナントの認証情報が必要なため、確認はユーザーが行う。エージェントは 🧪 までとする
- 補足: ダミーの認証情報で起動し、`/` と `/css/style.css` が200、`/profile` から `/oauth2/authorization/azure` を経て `login.microsoftonline.com` の認可エンドポイントへ302で転送されることを確認した
- 未確認: 実テナントでのログイン、コールバック後のプロフィール表示、ログアウト

### TASK-008

- 注意: CI/CD の変更にあたるため、変更内容をユーザーに確認してから反映する

### TASK-009

- 補足: README.md は技術スタックの表、主要な依存関係、前提条件、トラブルシューティングの Java の版を直す
- 注意: SOFTWARE_ARCHITECTURE.md には既存の markdownlint 指摘が51件あり、編集時にあわせて直す

## Backlog一覧

| ID | Status | Summary | DependsOn |
| --- | --- | --- | --- |
| BACKLOG-001 | ⏳ | Spring Boot 4.2 と対応する Spring Cloud Azure に更新する | TASK-010 |

## Backlog詳細（補足が必要な場合のみ）

### BACKLOG-001

- 補足: Spring Boot 4.2.x は2026-11-30にリリース予定。Spring Cloud Azure の対応版が出てから着手する
