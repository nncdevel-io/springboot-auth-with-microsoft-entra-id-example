# springboot-auth-with-microsoft-entra-id-example
Webアプリケーションの認証をMicrosoft Entra IDを利用する実装例

## 概要

このプロジェクトは、Spring BootアプリケーションでMicrosoft Entra ID（旧Azure Active Directory）を使用したOAuth2認証を実装するサンプルアプリケーションです。

## 前提条件

- Java 21以上
- Maven 3.x以上
- Microsoftアカウント（Azure Portal へのアクセス権限）
- Microsoft Entra IDテナント

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
|------|--------|------|
| **名前** | `springboot-auth-example` | 任意のアプリケーション名（識別しやすい名前を推奨） |
| **サポートされるアカウントの種類** | この組織ディレクトリのみに含まれるアカウント（シングルテナント） | 組織内ユーザーのみアクセス可能 |
| **リダイレクトURI** | **Web** - `http://localhost:8080/login/oauth2/code/azure` | Spring Securityが認証後にリダイレクトするURI |

#### リダイレクトURIの詳細

- **プラットフォーム**: `Web` を選択
- **ローカル環境用URI**: `http://localhost:8080/login/oauth2/code/azure`
- **本番環境用URI（後で追加）**: `https://yourdomain.com/login/oauth2/code/azure`

**重要**: Spring SecurityのOAuth2クライアント標準のリダイレクトURIパターンは `/login/oauth2/code/{registrationId}` です。

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
   - **フロントチャネルログアウトURL**: `http://localhost:8080` （オプション）
   - **暗黙的な許可およびハイブリッドフロー**: 通常は不要（チェックなし）
   - **パブリッククライアントフローを許可する**: いいえ

### 4. 登録情報の取得

アプリケーション登録完了後、以下の情報を取得してください：

| 情報 | 取得場所 | 用途 |
|------|----------|------|
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

```bash
# ローカルプロファイルで起動
mvn spring-boot:run -Dspring-boot.run.profiles=local

# または通常起動（application.propertiesのみ使用）
mvn spring-boot:run
```

### 4. アプリケーションへのアクセス

ブラウザで以下のURLにアクセス：

```
http://localhost:8080
```

## 動作確認

1. トップページ（`http://localhost:8080`）にアクセス
2. 「ログイン」リンクをクリック
3. Microsoft Entra IDのログイン画面が表示される
4. 組織アカウントでログイン
5. 初回ログイン時は同意画面が表示される場合があります
6. ログイン成功後、プロフィールページ（`/profile`）にアクセス可能

## トラブルシューティング

### リダイレクトURIエラー

**エラー**: `AADSTS50011: The reply URL specified in the request does not match the reply URLs configured for the application`

**解決方法**: Azure Portalのアプリ登録で、リダイレクトURIが正しく設定されているか確認してください。

### 認証エラー

**エラー**: `AADSTS7000215: Invalid client secret is provided`

**解決方法**: クライアントシークレットが正しいか、有効期限が切れていないか確認してください。

### アクセス許可エラー

**エラー**: 同意画面で「管理者の承認が必要です」と表示される

**解決方法**: Azure Portalで管理者の同意を付与してください。

## セキュリティ上の注意事項

1. **クライアントシークレットの管理**
   - クライアントシークレットは絶対にGitにコミットしない
   - `.gitignore` に `application-local.properties` が含まれていることを確認
   - 本番環境では環境変数または Azure Key Vault を使用

2. **本番環境への移行**
   - HTTPS を必ず使用
   - リダイレクトURIを本番ドメインに変更
   - 環境変数で認証情報を管理

## ライセンス

このプロジェクトはサンプル実装であり、教育目的で使用してください。