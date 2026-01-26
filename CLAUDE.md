# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## プロジェクト概要

Microsoft Entra ID（旧Azure AD）によるOAuth2/OpenID Connect認証を実装したSpring Boot Webアプリケーションのサンプル。Java 21、Spring Boot 3.5.x、Spring Cloud Azure 5.18.x を使用。

## ビルド・テストコマンド

```bash
# ビルド（コンパイルのみ）
./mvnw clean compile

# テスト実行
./mvnw test

# 単一テストクラス実行
./mvnw test -Dtest=SecurityConfigTests

# 単一テストメソッド実行
./mvnw test -Dtest=SecurityConfigTests#publicEndpointsAccessible

# パッケージング（JAR生成）
./mvnw clean package

# ローカル実行（localプロファイル使用）
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

## ローカル開発環境セットアップ

`src/main/resources/application-local.properties.example` を `application-local.properties` にコピーし、Azure Entra ID のテナントID・クライアントID・クライアントシークレットを設定する。`application-local.properties` は `.gitignore` に含まれている。

## アーキテクチャ

### パッケージ構成

```
io.nncdevel.example.auth/
├── Application.java                 # エントリポイント
├── config/
│   └── SecurityConfig.java          # Spring Security + OAuth2/Entra ID設定
└── controller/
    ├── HomeController.java          # "/" - 公開エンドポイント
    └── ProfileController.java       # "/profile" - 認証必須エンドポイント
```

### 認証フロー

1. 未認証ユーザーが `/profile` にアクセス → Spring Security が Entra ID ログインページにリダイレクト
2. Entra ID で認証後、IDトークン(JWT)が `/login/oauth2/code/azure` に返却
3. Spring Security がトークンを検証しセッションを確立

### エンドポイントのアクセス制御（SecurityConfig.java）

- **公開**: `/`, `/css/**`, `/error`
- **認証必須**: `/profile`（およびその他すべてのパス）

### ビュー層

Thymeleaf テンプレート (`src/main/resources/templates/`) + 静的リソース (`src/main/resources/static/css/`)。`thymeleaf-extras-springsecurity6` で認証状態に応じた表示切替を行う。

### 設定の優先順位

1. 環境変数 (`AZURE_TENANT_ID`, `AZURE_CLIENT_ID`, `AZURE_CLIENT_SECRET`)
2. `application-local.properties`（localプロファイル時）
3. `application.properties`（ベース設定、プレースホルダー使用）

### テスト構成

`@SpringBootTest` + `@AutoConfigureMockMvc` による統合テスト。OAuth2認証のモックには `SecurityMockMvcRequestPostProcessors.oauth2Login()` を使用。

## CI/CD

GitHub Actions (`.github/workflows/ci.yml`)。`main` ブランチへのpush/PRおよび `claude/**` ブランチへのpushで起動。JDK 21 (Temurin) を使用。
