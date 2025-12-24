# 実装タスク一覧

## タスク管理表

| タスクID | タスク内容 | ステータス |
|---------|-----------|----------|
| TASK-001 | Mavenプロジェクト基盤の構築（pom.xml作成） | 完了 |
| TASK-002 | ディレクトリ構造の作成 | 完了 |
| TASK-003 | .gitignoreの更新（Spring Boot対応） | 完了 |
| TASK-004 | アプリケーション設定ファイルの作成（application.yml） | 未着手 |
| TASK-005 | ローカル開発用設定ファイルのテンプレート作成 | 未着手 |
| TASK-006 | Spring Bootメインクラスの実装 | 未着手 |
| TASK-007 | セキュリティ設定クラスの実装 | 未着手 |
| TASK-008 | Homeコントローラーの実装 | 未着手 |
| TASK-009 | Profileコントローラーの実装 | 未着手 |
| TASK-010 | ビューテンプレートの作成（index.html） | 未着手 |
| TASK-011 | ビューテンプレートの作成（profile.html） | 未着手 |
| TASK-012 | ビューテンプレートの作成（error.html）とCSSファイル | 未着手 |
| TASK-013 | 単体テストクラスの作成 | 未着手 |
| TASK-014 | README.mdの更新（セットアップ手順の追加） | 未着手 |
| TASK-015 | 動作確認とドキュメント最終化 | 未着手 |

## ステータス定義

- **未着手**: まだ作業を開始していない
- **進行中**: 現在作業中
- **レビュー待ち**: 実装完了、レビュー待ち
- **完了**: レビュー完了、マージ済み
- **保留**: 何らかの理由で作業を保留中

## タスク詳細

### TASK-001: Mavenプロジェクト基盤の構築

**目的**: Spring Bootプロジェクトの基盤となるpom.xmlを作成

**作業内容**:
- pom.xmlの作成
- Spring Boot Parent POMの設定（3.4.1）
- Java 21の設定
- 必要な依存関係の追加：
  - spring-boot-starter-web
  - spring-boot-starter-security
  - spring-boot-starter-oauth2-client
  - spring-cloud-azure-starter-active-directory
  - spring-boot-starter-thymeleaf
  - spring-boot-starter-test
- Spring Cloud Azure Dependencies BOMの追加
- Mavenプラグインの設定

**成果物**: `pom.xml`

**見積もり工数**: 小

---

### TASK-002: ディレクトリ構造の作成

**目的**: Mavenプロジェクトの標準ディレクトリ構造を構築

**作業内容**:
- `src/main/java/io/nncdevel/example/auth` ディレクトリの作成
- `src/main/java/io/nncdevel/example/auth/config` ディレクトリの作成
- `src/main/java/io/nncdevel/example/auth/controller` ディレクトリの作成
- `src/main/resources` ディレクトリの作成
- `src/main/resources/static/css` ディレクトリの作成
- `src/main/resources/templates` ディレクトリの作成
- `src/test/java/io/nncdevel/example/auth` ディレクトリの作成

**成果物**: プロジェクトディレクトリ構造

**見積もり工数**: 小

---

### TASK-003: .gitignoreの更新

**目的**: Spring BootプロジェクトとMaven用の除外設定を追加

**作業内容**:
- Mavenビルド成果物の除外設定追加（target/）
- IDE固有ファイルの除外設定追加（.idea/, .vscode/, *.iml）
- 設定ファイルの除外設定追加（application-local.yml）
- OSファイルの除外設定追加（.DS_Store, Thumbs.db）

**成果物**: 更新された`.gitignore`

**見積もり工数**: 小

---

### TASK-004: アプリケーション設定ファイルの作成

**目的**: Spring Bootアプリケーションの基本設定とMicrosoft Entra ID接続設定を定義

**作業内容**:
- `application.yml`の作成
- Spring Cloudアプリケーション名の設定
- サーバーポート設定（8080）
- Microsoft Entra ID設定：
  - テナントIDのプレースホルダー
  - クライアントIDのプレースホルダー
  - クライアントシークレットのプレースホルダー
  - 認証グループの設定
  - リダイレクトURI設定
- ログレベル設定

**成果物**: `src/main/resources/application.yml`

**見積もり工数**: 中

---

### TASK-005: ローカル開発用設定ファイルのテンプレート作成

**目的**: 開発者がローカル環境で使用する設定ファイルのテンプレートを提供

**作業内容**:
- `application-local.yml.example`の作成
- 実際の値を入れる箇所にコメントを記載
- Git管理外とするための.gitignore設定確認

**成果物**: `src/main/resources/application-local.yml.example`

**見積もり工数**: 小

---

### TASK-006: Spring Bootメインクラスの実装

**目的**: アプリケーションのエントリーポイントを実装

**作業内容**:
- `Application.java`の作成
- `@SpringBootApplication`アノテーションの付与
- mainメソッドの実装

**成果物**: `src/main/java/io/nncdevel/example/auth/Application.java`

**見積もり工数**: 小

---

### TASK-007: セキュリティ設定クラスの実装

**目的**: Spring SecurityとMicrosoft Entra IDの統合設定を実装

**作業内容**:
- `SecurityConfig.java`の作成
- `@Configuration`と`@EnableWebSecurity`アノテーションの付与
- SecurityFilterChainの定義
- Microsoft Entra ID用セキュリティ設定：
  - `AadWebApplicationHttpSecurityConfigurer`の適用
  - 認証が必要なエンドポイントの定義（/profile）
  - 公開エンドポイントの定義（/, /css/**, /error）
  - OAuth2ログインの有効化
  - ログアウト設定
- CSRF保護の設定

**成果物**: `src/main/java/io/nncdevel/example/auth/config/SecurityConfig.java`

**見積もり工数**: 中

---

### TASK-008: Homeコントローラーの実装

**目的**: トップページを表示するコントローラーを実装

**作業内容**:
- `HomeController.java`の作成
- `@Controller`アノテーションの付与
- `/`エンドポイントの実装
- ビュー名の返却（"index"）
- 認証状態の確認と情報のModelへの追加

**成果物**: `src/main/java/io/nncdevel/example/auth/controller/HomeController.java`

**見積もり工数**: 小

---

### TASK-009: Profileコントローラーの実装

**目的**: 認証後のユーザープロフィール情報を表示するコントローラーを実装

**作業内容**:
- `ProfileController.java`の作成
- `@Controller`アノテーションの付与
- `/profile`エンドポイントの実装
- OAuth2認証情報の取得
- ユーザー情報（名前、メールアドレス等）のModelへの追加
- ビュー名の返却（"profile"）

**成果物**: `src/main/java/io/nncdevel/example/auth/controller/ProfileController.java`

**見積もり工数**: 中

---

### TASK-010: ビューテンプレートの作成（index.html）

**目的**: トップページのHTMLテンプレートを作成

**作業内容**:
- `index.html`の作成（Thymeleaf形式）
- ページタイトルとヘッダーの追加
- 認証状態に応じた表示切り替え：
  - 未認証時: ログインリンクの表示
  - 認証済み: ユーザー名とプロフィールリンクの表示
- スタイルシートのリンク

**成果物**: `src/main/resources/templates/index.html`

**見積もり工数**: 中

---

### TASK-011: ビューテンプレートの作成（profile.html）

**目的**: ユーザープロフィールページのHTMLテンプレートを作成

**作業内容**:
- `profile.html`の作成（Thymeleaf形式）
- ユーザー情報の表示：
  - 名前
  - メールアドレス
  - ユーザーID
- ログアウトリンクの追加
- ホームへ戻るリンクの追加
- スタイルシートのリンク

**成果物**: `src/main/resources/templates/profile.html`

**見積もり工数**: 中

---

### TASK-012: ビューテンプレートの作成（error.html）とCSSファイル

**目的**: エラーページとスタイルシートを作成

**作業内容**:
- `error.html`の作成（Thymeleaf形式）
- エラーメッセージの表示
- ホームへ戻るリンクの追加
- `style.css`の作成
- 基本的なスタイリング（レイアウト、ボタン、リンク等）
- レスポンシブデザイン対応

**成果物**:
- `src/main/resources/templates/error.html`
- `src/main/resources/static/css/style.css`

**見積もり工数**: 中

---

### TASK-013: 単体テストクラスの作成

**目的**: アプリケーションの基本的なテストを実装

**作業内容**:
- `ApplicationTests.java`の作成
- `@SpringBootTest`アノテーションの付与
- コンテキストロードテストの実装
- セキュリティ設定の基本テスト

**成果物**: `src/test/java/io/nncdevel/example/auth/ApplicationTests.java`

**見積もり工数**: 中

---

### TASK-014: README.mdの更新

**目的**: プロジェクトのセットアップ手順と使用方法を記載

**作業内容**:
- プロジェクト概要の拡充
- 前提条件の記載（Java 21, Maven 3.x）
- Microsoft Entra IDアプリ登録手順の記載
- ローカル環境セットアップ手順の記載
- 環境変数設定方法の記載
- アプリケーション起動方法の記載
- アクセスURL情報の記載
- トラブルシューティング情報の追加

**成果物**: 更新された`README.md`

**見積もり工数**: 中

---

### TASK-015: 動作確認とドキュメント最終化

**目的**: 全体の動作確認と最終調整

**作業内容**:
- Mavenビルドの実行とエラー確認
- アプリケーションの起動確認
- 認証フローの動作確認（可能な範囲で）
- ドキュメントの整合性確認
- コードフォーマットの統一
- 不要なコメントの削除

**成果物**: 完成したアプリケーション

**見積もり工数**: 中

---

## タスク依存関係

```
TASK-001 (pom.xml作成)
  ↓
TASK-002 (ディレクトリ作成)
  ↓
TASK-003 (.gitignore更新) ── 並行 ── TASK-006 (Applicationクラス)
  ↓                                    ↓
TASK-004 (application.yml)         TASK-007 (SecurityConfig)
  ↓                                    ↓
TASK-005 (local設定テンプレート)    TASK-008, 009 (Controllers)
  ↓                                    ↓
TASK-010, 011, 012 (Templates & CSS)
  ↓
TASK-013 (Tests)
  ↓
TASK-014 (README更新)
  ↓
TASK-015 (動作確認)
```

## 注意事項

1. **Microsoft Entra ID設定**: TASK-004以降を進める前に、Azure Portalでアプリ登録を完了しておくことを推奨
2. **ローカルテスト**: 実際のMicrosoft Entra IDとの連携テストは、適切な認証情報が設定されている場合のみ可能
3. **セキュリティ**: クライアントシークレットや認証情報は絶対にGitにコミットしない
4. **レビュー**: 各タスク完了後、コードレビューを実施することを推奨

## 進捗管理

- 各タスクの開始時にステータスを「進行中」に更新
- タスク完了時にステータスを「レビュー待ち」に更新
- レビュー完了後、ステータスを「完了」に更新
- ブロッカーがある場合はステータスを「保留」に設定し、その理由を明記
