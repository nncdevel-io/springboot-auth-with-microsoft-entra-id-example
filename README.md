# springboot-auth-with-microsoft-entra-id-example

[![CI](https://github.com/nncdevel-io/springboot-auth-with-microsoft-entra-id-example/actions/workflows/ci.yml/badge.svg)](https://github.com/nncdevel-io/springboot-auth-with-microsoft-entra-id-example/actions/workflows/ci.yml)

Webアプリケーションの認証をMicrosoft Entra IDを利用する実装例

## 概要

このプロジェクトは、Spring BootアプリケーションでMicrosoft Entra ID（旧Azure Active Directory）を使用したOAuth2認証を実装するサンプルアプリケーションです。

## 技術スタック

| カテゴリ | 技術 | バージョン |
| --------- | ------ | ----------- |
| **言語** | Java | 21 |
| **フレームワーク** | Spring Boot | 3.5.9 |
| **ビルドツール** | Maven Wrapper | 3.x |
| **認証** | Microsoft Entra ID (Azure AD) | - |
| **OAuth2** | Spring Security OAuth2 Client | 3.5.9 |
| **Azure統合** | Spring Cloud Azure Active Directory | 5.18.0 |
| **テンプレートエンジン** | Thymeleaf | 3.5.9 |
| **セキュリティ** | Spring Security | 6.x |

### 主要な依存関係

- `spring-boot-starter-web` - Spring MVC Webアプリケーション
- `spring-boot-starter-security` - Spring Security
- `spring-boot-starter-oauth2-client` - OAuth2/OpenID Connectクライアント
- `spring-cloud-azure-starter-active-directory` - Microsoft Entra ID統合
  - （`azure-core-http-netty` を除外）
- `azure-core-http-jdk-httpclient` - Azure SDK用HTTPクライアント（JDK標準HttpClient使用、Nettyの代替）
- `spring-boot-starter-thymeleaf` - Thymeleafテンプレートエンジン
- `thymeleaf-extras-springsecurity6` - ThymeleafとSpring Securityの統合

## 前提条件

- Java 21以上
- Microsoftアカウント（Azure Portal へのアクセス権限）
- Microsoft Entra IDテナント

**注**: Maven Wrapperを使用するため、Mavenの事前インストールは不要です。

## プロジェクト構成

```text
springboot-auth-with-microsoft-entra-id-example/
├── .mvn/
│   ├── jvm.config.example               # Maven JVMオプション設定テンプレート（プロキシ等）
│   └── wrapper/
│       └── maven-wrapper.properties     # Maven Wrapper設定
├── mvnw                             # Maven Wrapper スクリプト (Unix/Linux/Mac)
├── mvnw.cmd                         # Maven Wrapper スクリプト (Windows)
├── pom.xml                          # Mavenプロジェクト設定ファイル
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── io/nncdevel/example/auth/
│   │   │       ├── Application.java                      # Spring Bootメインクラス
│   │   │       ├── config/
│   │   │       │   └── SecurityConfig.java              # セキュリティ設定
│   │   │       └── controller/
│   │   │           ├── HomeController.java              # トップページコントローラー
│   │   │           └── ProfileController.java           # プロフィールページコントローラー
│   │   └── resources/
│   │       ├── application.properties                    # アプリケーション設定
│   │       ├── application-local.properties.example      # ローカル設定テンプレート
│   │       ├── static/
│   │       │   └── css/
│   │       │       └── style.css                        # スタイルシート
│   │       └── templates/
│   │           ├── index.html                           # トップページ
│   │           ├── profile.html                         # プロフィールページ
│   │           └── error.html                           # エラーページ
│   └── test/
│       └── java/
│           └── io/nncdevel/example/auth/
│               ├── ApplicationTests.java                # アプリケーションテスト
│               ├── config/
│               │   └── SecurityConfigTests.java         # セキュリティ設定テスト
│               └── controller/
│                   ├── HomeControllerTests.java         # Homeコントローラーテスト
│                   └── ProfileControllerTests.java      # Profileコントローラーテスト
└── README.md
```

### 主要コンポーネント

- **Application.java**: Spring Bootアプリケーションのエントリーポイント
- **SecurityConfig.java**: Microsoft Entra IDとSpring Securityの統合設定
- **HomeController.java**: 公開トップページ（`/`）のコントローラー
- **ProfileController.java**: 認証後のプロフィールページ（`/profile`）のコントローラー
- **application.properties**: アプリケーション全体の設定（プレースホルダー含む）
- **application-local.properties**: ローカル開発用の設定（Git管理外）

## Microsoft Entra ID アプリケーション登録

アプリケーションを実行する前に、Azure PortalでMicrosoft Entra IDアプリケーションを登録する必要があります。

### 1. Azure Portal へアクセス

1. [Azure Portal](https://portal.azure.com/) にサインイン
2. 「Microsoft Entra ID」サービスに移動
3. 左側メニューから「アプリの登録」を選択
4. 「新規登録」をクリック

### 2. アプリケーション登録時に必要な情報

以下の情報を入力してアプリケーションを登録します：

| 項目 | 設定値 | 説明 |
| ------ | -------- | ------ |
| **名前** | `springboot-auth-example` | 任意のアプリケーション名（識別しやすい名前を推奨） |
| **サポートされるアカウントの種類** | この組織ディレクトリのみに含まれるアカウント（シングルテナント） | 組織内ユーザーのみアクセス可能 |
| **リダイレクトURI** | **Web** - `http://localhost:8080/login/oauth2/code/azure` | Spring Securityが認証後にリダイレクトするURI |

#### リダイレクトURIの詳細

- **プラットフォーム**: `Web` を選択
- **ローカル環境用URI**: `http://localhost:8080/login/oauth2/code/azure`
- **本番環境用URI（後で追加）**: `https://yourdomain.com/login/oauth2/code/azure`

**重要**: Spring SecurityのOAuth2クライアント標準の
リダイレクトURIパターンは
`/login/oauth2/code/{registrationId}` です。

### 3. アプリケーション登録後の追加設定

アプリケーション登録後、以下の設定を行います。

#### 3-1. クライアントシークレットの作成

1. 登録したアプリケーションの詳細画面で「証明書とシークレット」を選択
2. 「新しいクライアントシークレット」をクリック
3. 説明（例：`local-dev-secret`）と有効期限を設定
4. 「追加」をクリック
5. **重要**: 表示された「値」を必ずコピーして保存（この画面を離れると二度と表示されません）

#### 3-2. APIのアクセス許可の設定

1. 「APIのアクセス許可」を選択
2. 「アクセス許可の追加」をクリック
3. 「Microsoft Graph」を選択
4. 「委任されたアクセス許可」を選択
5. 以下のアクセス許可を追加：
   - `User.Read` - サインインとユーザープロフィールの読み取り
   - `openid` - OpenID Connect サインイン
   - `profile` - ユーザーの基本プロファイルの表示
   - `email` - ユーザーのメールアドレスの表示
6. 「アクセス許可の追加」をクリック
7. **「（組織名）に管理者の同意を与えます」をクリック**（管理者権限が必要）

#### 3-3. 認証設定（オプション）

1. 「認証」を選択
2. 以下を確認・設定：
   - **フロントチャネルログアウトURL**: `http://localhost:8080`
   - **暗黙的な許可およびハイブリッドフロー**: 通常は不要（チェックなし）
   - **パブリッククライアントフローを許可する**: いいえ

### 4. 登録情報の取得

アプリケーション登録完了後、以下の情報を取得してください：

| 情報 | 取得場所 | 用途 |
| ------ | ---------- | ------ |
| **テナントID（ディレクトリID）** | 「概要」ページ | `spring.cloud.azure.active-directory.profile.tenant-id` |
| **アプリケーション（クライアント）ID** | 「概要」ページ | `spring.cloud.azure.active-directory.credential.client-id` |
| **クライアントシークレット（値）** | 「証明書とシークレット」で作成時に表示 | `spring.cloud.azure.active-directory.credential.client-secret` |

**注意**: これらの値は機密情報です。Gitにコミットしないでください。

## ローカル開発環境のセットアップ

### 1. 設定ファイルの準備

テンプレートファイルをコピーして、ローカル設定ファイルを作成します：

```bash
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties
```

### 2. Azure情報の設定

`src/main/resources/application-local.properties` を開き、取得した値を設定します：

```properties
spring.cloud.azure.active-directory.profile.tenant-id=<your-tenant-id>
spring.cloud.azure.active-directory.credential.client-id=<your-client-id>
spring.cloud.azure.active-directory.credential.client-secret=<your-client-secret>
```

### 3. アプリケーションの起動

**Unix/Linux/Mac:**

```bash
# ローカルプロファイルで起動
./mvnw spring-boot:run -Dspring-boot.run.profiles=local

# または通常起動（application.propertiesのみ使用）
./mvnw spring-boot:run
```

**Windows:**

```cmd
# ローカルプロファイルで起動
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local

# または通常起動（application.propertiesのみ使用）
mvnw.cmd spring-boot:run
```

### 4. アプリケーションへのアクセス

ブラウザで以下のURLにアクセス：

```text
http://localhost:8080
```

## 動作確認

1. トップページ（`http://localhost:8080`）にアクセス
2. 「ログイン」リンクをクリック
3. Microsoft Entra IDのログイン画面が表示される
4. 組織アカウントでログイン
5. 初回ログイン時は同意画面が表示される場合があります
6. ログイン成功後、プロフィールページ（`/profile`）にアクセス可能

## ビルドとテスト

### プロジェクトのビルド

**Unix/Linux/Mac:**

依存関係のダウンロードとコンパイルを実行：

```bash
./mvnw clean compile
```

実行可能JARファイルのビルド：

```bash
./mvnw clean package
```

**Windows:**

依存関係のダウンロードとコンパイルを実行：

```cmd
mvnw.cmd clean compile
```

実行可能JARファイルのビルド：

```cmd
mvnw.cmd clean package
```

ビルド成果物は
`target/springboot-auth-with-microsoft-entra-id-example-0.0.1-SNAPSHOT.jar`
に生成されます。

### テストの実行

**Unix/Linux/Mac:**

すべてのテストを実行：

```bash
./mvnw test
```

特定のテストクラスのみ実行：

```bash
# ApplicationTestsのみ実行
./mvnw test -Dtest=ApplicationTests

# HomeControllerTestsのみ実行
./mvnw test -Dtest=HomeControllerTests

# ProfileControllerTestsのみ実行
./mvnw test -Dtest=ProfileControllerTests
```

**Windows:**

すべてのテストを実行：

```cmd
mvnw.cmd test
```

特定のテストクラスのみ実行：

```cmd
rem ApplicationTestsのみ実行
mvnw.cmd test -Dtest=ApplicationTests

rem HomeControllerTestsのみ実行
mvnw.cmd test -Dtest=HomeControllerTests

rem ProfileControllerTestsのみ実行
mvnw.cmd test -Dtest=ProfileControllerTests
```

### テストカバレッジ

このプロジェクトには以下のテストが含まれています：

- **ApplicationTests**: Spring Bootアプリケーションコンテキストのロードテスト
- **SecurityConfigTests**: セキュリティ設定の検証
- **HomeControllerTests**: Homeコントローラーのテスト（認証なし/ありの表示確認）
- **ProfileControllerTests**: Profileコントローラーのテスト（認証必須の確認）

### ビルドとテストを一度に実行

**Unix/Linux/Mac:**

```bash
./mvnw clean install
```

**Windows:**

```cmd
mvnw.cmd clean install
```

このコマンドは、クリーン→コンパイル→テスト→パッケージングを順番に実行します。

### ビルド済みJARファイルの実行

```bash
java -jar target/springboot-auth-with-microsoft-entra-id-example-0.0.1-SNAPSHOT.jar
```

ローカルプロファイルで実行：

```bash
java -jar target/springboot-auth-with-microsoft-entra-id-example-0.0.1-SNAPSHOT.jar --spring.profiles.active=local
```

## トラブルシューティング

### リダイレクトURIエラー

**エラー**:

```text
AADSTS50011: The reply URL specified in the request does not match the reply URLs configured for the application
```

**原因**: Azure Portalに登録されたリダイレクトURIとアプリケーションが使用するURIが一致していません。

**解決方法**:

1. `Azure Portal > Microsoft Entra ID > アプリの登録 > 該当アプリ > 認証`を開く
2. リダイレクトURIに `http://localhost:8080/login/oauth2/code/azure` が登録されているか確認
3. プラットフォームが「Web」になっているか確認
4. 設定を保存して、アプリケーションを再起動

### 認証エラー

**エラー**: `AADSTS7000215: Invalid client secret is provided`

**原因**: クライアントシークレットが間違っているか、有効期限が切れています。

**解決方法**:

1. `Azure Portal > Microsoft Entra ID > アプリの登録 > 該当アプリ > 証明書とシークレット` を開く
2. 既存のシークレットの有効期限を確認
3. 期限切れの場合は新しいシークレットを作成
4. `application-local.properties` のシークレット値を更新
5. アプリケーションを再起動

### アクセス許可エラー

**エラー**: 同意画面で「管理者の承認が必要です」と表示される

**原因**: 組織のポリシーにより、ユーザーが自分でアプリケーションに同意できない設定になっています。

**解決方法**:

1. `Azure Portal > Microsoft Entra ID > アプリの登録 > 該当アプリ > APIのアクセス許可`を開く
2. 「（組織名）に管理者の同意を与えます」ボタンをクリック（管理者権限が必要）
3. 同意が完了したら、再度ログインを試す

### 起動エラー（ポート競合）

**エラー**: `Web server failed to start. Port 8080 was already in use.`

**原因**: すでに別のアプリケーションがポート8080を使用しています。

**解決方法**:

1. 別のポートを使用する場合（Unix/Linux/Mac）：

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
   ```

   Windows:

   ```cmd
   mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
   ```

2. または、`application-local.properties` に以下を追加：

   ```properties
   server.port=8081
   ```

### 設定ファイルが見つからないエラー

**エラー**: `Could not resolve placeholder 'AZURE_TENANT_ID'`

**原因**: 環境変数または設定ファイルにAzure設定が見つかりません。

**解決方法**:

1. `application-local.properties` ファイルが存在するか確認
2. ファイルに正しい値が設定されているか確認
3. ローカルプロファイルで起動しているか確認：

   Unix/Linux/Mac:

   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=local
   ```

   Windows:

   ```cmd
   mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=local
   ```

### ビルドエラー

**エラー**: `Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin`

**原因**: Java バージョンが21未満です。

**解決方法**:

1. Javaバージョンを確認：

   ```bash
   java -version
   ```

2. Java 21以上をインストール
3. `JAVA_HOME` 環境変数を正しく設定

### SSL証明書エラー（企業プロキシ環境）

**エラー**:

```text
PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
```

**原因**: 企業プロキシのSSLインスペクションにより、プロキシの独自CA証明書がJVMに信頼されていません。

**解決方法**:

- 独自CA証明書をJVMのcacertsにインポートする
- macOSの場合: `-Djavax.net.ssl.trustStoreType=KeychainStore` を指定
- Windowsの場合: `-Djavax.net.ssl.trustStoreType=Windows-ROOT` を指定

詳細は「[企業プロキシ環境での利用](docs/CORPORATE_PROXY.md)」を参照してください。

### ログレベルの調整

問題の詳細を確認するため、ログレベルを上げることができます：

```properties
# application-local.properties に追加
logging.level.io.nncdevel.example.auth=TRACE
logging.level.org.springframework.security=TRACE
logging.level.com.azure.spring=TRACE
```

## セキュリティ上の注意事項

1. **クライアントシークレットの管理**
   - クライアントシークレットは絶対にGitにコミットしない
   - `.gitignore` に `application-local.properties` が含まれていることを確認
   - 本番環境では環境変数または Azure Key Vault を使用

2. **本番環境への移行**
   - HTTPS を必ず使用
   - リダイレクトURIを本番ドメインに変更
   - 環境変数で認証情報を管理

## 関連ドキュメント

- [企業プロキシ環境での利用](docs/CORPORATE_PROXY.md) — プロキシ設定、独自CA証明書、Spring Boot起動時のJVM引数

## ライセンス

このプロジェクトはサンプル実装であり、教育目的で使用してください。
