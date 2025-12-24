# ソフトウェアアーキテクチャ設計書

## 1. 概要

本アプリケーションは、Microsoft Entra ID（旧Azure AD）を使用した認証機能を持つSpring Boot Webアプリケーションです。OAuth 2.0 / OpenID Connectプロトコルを利用して、Microsoftアカウントでのシングルサインオン（SSO）を実現します。

## 2. 技術スタック

### 2.1 開発環境
- **Java**: 21
- **ビルドツール**: Maven
- **フレームワーク**: Spring Boot 3.4.1（最新安定版）

### 2.2 主要ライブラリ
- **Spring Boot Starter Web**: Webアプリケーション基盤
- **Spring Boot Starter Security**: セキュリティ機能
- **Spring Boot Starter OAuth2 Client**: OAuth2クライアント機能
- **Spring Cloud Azure Starter Active Directory**: Microsoft Entra ID統合
- **Spring Boot Starter Thymeleaf**: ビューテンプレートエンジン
- **Spring Boot Starter Test**: テストフレームワーク

## 3. アーキテクチャ設計

### 3.1 レイヤー構造

```
┌─────────────────────────────────────────┐
│       Presentation Layer                │
│  (Controllers / Thymeleaf Templates)    │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│         Service Layer                   │
│      (Business Logic - Optional)        │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│      Security Configuration Layer       │
│  (OAuth2 + Entra ID Integration)        │
└─────────────────────────────────────────┘
                  ↓
┌─────────────────────────────────────────┐
│      Microsoft Entra ID                 │
│    (Authentication Provider)            │
└─────────────────────────────────────────┘
```

### 3.2 主要コンポーネント

#### 3.2.1 Application Entry Point
- **クラス名**: `Application.java`
- **役割**: Spring Bootアプリケーションのエントリーポイント
- **パッケージ**: `com.example.auth`

#### 3.2.2 Security Configuration
- **クラス名**: `SecurityConfig.java`
- **役割**:
  - Spring Securityの設定
  - Microsoft Entra ID認証の統合
  - 認証が必要なエンドポイントの定義
  - OAuth2ログインの設定
- **パッケージ**: `com.example.auth.config`

#### 3.2.3 Controllers
- **HomeController.java**
  - **役割**: トップページの表示
  - **エンドポイント**: `/`
  - **パッケージ**: `com.example.auth.controller`

- **ProfileController.java**
  - **役割**: 認証後のプロフィール情報表示
  - **エンドポイント**: `/profile`
  - **パッケージ**: `com.example.auth.controller`

#### 3.2.4 Configuration Files
- **application.yml**
  - Spring Bootアプリケーション設定
  - Microsoft Entra ID接続情報
  - OAuth2クライアント設定

- **application-local.yml**
  - ローカル開発環境用設定（Git管理外）

### 3.3 ディレクトリ構造

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── example/
│   │           └── auth/
│   │               ├── Application.java
│   │               ├── config/
│   │               │   └── SecurityConfig.java
│   │               └── controller/
│   │                   ├── HomeController.java
│   │                   └── ProfileController.java
│   └── resources/
│       ├── application.yml
│       ├── application-local.yml.example
│       ├── static/
│       │   └── css/
│       │       └── style.css
│       └── templates/
│           ├── index.html
│           ├── profile.html
│           └── error.html
└── test/
    └── java/
        └── com/
            └── example/
                └── auth/
                    └── ApplicationTests.java
```

## 4. 認証フロー

### 4.1 OpenID Connect認証シーケンス

```
User          →  Application      →  Microsoft Entra ID
  |                   |                      |
  |-- アクセス -----→  |                      |
  |                   |                      |
  |                   |-- 認証リダイレクト --→ |
  |                   |                      |
  |←---------------- ログイン画面 --------------|
  |                   |                      |
  |-- 認証情報入力 ----------------------→   |
  |                   |                      |
  |                   |←- ID Token / Code ---|
  |                   |                      |
  |←-- 認証完了 -------|                      |
```

### 4.2 認証フローの詳細

1. **初回アクセス**: ユーザーが保護されたリソースにアクセス
2. **リダイレクト**: Spring Securityがユーザーを Microsoft Entra ID ログインページにリダイレクト
3. **認証**: ユーザーがMicrosoftアカウントで認証
4. **トークン取得**: アプリケーションが認可コードを使用してIDトークンを取得
5. **セッション確立**: Spring Securityがセッションを確立し、ユーザー情報を保持
6. **アクセス許可**: 認証済みユーザーとして保護されたリソースへアクセス可能

## 5. セキュリティ設計

### 5.1 認証方式
- **プロトコル**: OAuth 2.0 / OpenID Connect
- **認証プロバイダー**: Microsoft Entra ID
- **トークンタイプ**: ID Token（JWT形式）

### 5.2 保護対象エンドポイント
- `/profile`: 認証が必要
- その他の管理者向けエンドポイント（将来拡張）

### 5.3 公開エンドポイント
- `/`: トップページ（認証不要）
- `/error`: エラーページ
- `/css/**`, `/js/**`: 静的リソース

### 5.4 セッション管理
- Spring Securityのデフォルトセッション管理を使用
- セッションタイムアウト: 30分（デフォルト）

## 6. 設定管理

### 6.1 環境変数による設定
本番環境では以下の情報を環境変数で管理：
- `AZURE_TENANT_ID`: Microsoft Entra テナントID
- `AZURE_CLIENT_ID`: アプリケーション（クライアント）ID
- `AZURE_CLIENT_SECRET`: クライアントシークレット

### 6.2 設定ファイル階層
1. `application.yml`: 共通設定
2. `application-local.yml`: ローカル開発環境設定（Git管理外）
3. 環境変数: 本番環境設定（最優先）

## 7. ログ設計

### 7.1 ログレベル
- **本番環境**: INFO
- **開発環境**: DEBUG

### 7.2 ログ出力内容
- 認証成功/失敗イベント
- アプリケーションエラー
- セキュリティ関連イベント

## 8. エラーハンドリング

### 8.1 認証エラー
- 認証失敗時はエラーページへリダイレクト
- エラーメッセージをログに記録

### 8.2 アプリケーションエラー
- Spring Bootのデフォルトエラーハンドリング機構を使用
- カスタムエラーページの提供

## 9. 拡張性考慮事項

### 9.1 将来的な機能拡張
- ロールベースアクセス制御（RBAC）の実装
- グループベースの権限管理
- API保護（Bearer Token認証）
- マルチテナント対応

### 9.2 スケーラビリティ
- ステートレス認証への移行（JWTベース）
- Redis等による分散セッション管理
- ロードバランサー対応

## 10. デプロイメント戦略

### 10.1 ローカル開発
- `mvn spring-boot:run`による起動
- ポート: 8080
- プロファイル: local

### 10.2 本番環境
- 実行可能JAR形式でのデプロイ
- Azure App Service / Azure Container Apps推奨
- 環境変数による設定注入
- HTTPS必須

## 11. 依存関係管理

### 11.1 Spring Boot BOM
Spring Boot Parent POMを使用し、互換性のあるバージョンを自動管理

### 11.2 Azure SDK BOM
Spring Cloud Azure Dependencies BOMを使用し、Azure関連ライブラリのバージョンを一元管理

## 12. テスト戦略

### 12.1 単体テスト
- JUnit 5使用
- MockMvcによるコントローラーテスト

### 12.2 統合テスト
- Spring Boot Testによる統合テスト
- セキュリティ設定のテスト

### 12.3 手動テスト
- Microsoft Entra IDとの実際の認証フローテスト
- ブラウザベースのE2Eテスト

## 13. ドキュメント管理

- **README.md**: プロジェクト概要、セットアップ手順
- **SOFTWARE_ARCHITECTURE.md**: 本ドキュメント
- **TASK.md**: 実装タスク一覧
- **Javadoc**: コードレベルのドキュメント（重要クラスのみ）
