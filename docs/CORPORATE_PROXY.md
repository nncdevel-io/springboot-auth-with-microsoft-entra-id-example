# 企業プロキシ環境での利用

企業ネットワーク内でフォワードプロキシやSSLインスペクション（独自CA証明書）が使用されている環境では、追加の設定が必要です。

## 1. Mavenのプロキシ設定（`.mvn/jvm.config`）

`.mvn/jvm.config` にJVMシステムプロパティを記述することで、Maven自体のHTTP通信（依存関係のダウンロード等）にプロキシを適用できます。

テンプレートファイルをコピーして、自身の環境に合わせて編集してください。

**Unix/Linux/Mac:**

```bash
cp .mvn/jvm.config.example .mvn/jvm.config
```

**Windows:**

```cmd
copy .mvn\jvm.config.example .mvn\jvm.config
```

`.mvn/jvm.config` は `.gitignore` に含まれているため、Git管理外となります。

## 2. 独自CA証明書のインポート

企業プロキシがSSLインスペクションを行っている場合、プロキシの独自CA証明書をJVMのトラストストアに登録する必要があります。

### 方法A: JVMのcacertsに直接インポート

**Unix/Linux/Mac:**

```bash
keytool -importcert -cacerts -storepass changeit \
  -alias proxy-ca -file "${SSL_CERT_FILE}" -noprompt
```

**Windows:**

```cmd
keytool -importcert -cacerts -storepass changeit ^
  -alias proxy-ca -file "%SSL_CERT_FILE%" -noprompt
```

- CA証明書ファイルのパスを環境に合わせて指定してください
- `changeit` はJVMのデフォルトキーストアパスワードです
- このコマンドは `$JAVA_HOME/lib/security/cacerts` に証明書を追加します

### 方法B: macOS Keychainを利用

macOSでは、キーチェーンアクセスに登録済みのCA証明書をJVMから直接利用できます。JVM起動時に以下を指定します。

```text
-Djavax.net.ssl.trustStoreType=KeychainStore
```

キーチェーンアクセスにCA証明書が「信頼」として登録されている場合、cacertsへのインポートなしでSSL通信が成功します。

### 方法C: Windowsの証明書ストアを利用

Windowsでは、OS の証明書ストアをJVMから利用できます。JVM起動時に以下を指定します。

```text
-Djavax.net.ssl.trustStoreType=Windows-ROOT
```

Windows証明書マネージャーの「信頼されたルート証明機関」にCA証明書が登録されている場合、この設定でSSL通信が成功します。

## 3. Spring Boot起動時のJVM引数

**重要**: `.mvn/jvm.config` はMavenプロセス自体のJVMオプションです。
`spring-boot:run` で起動するアプリケーションは別のJVMプロセスとして
フォークされるため、`.mvn/jvm.config` の設定は適用されません。

アプリケーションのJVMにプロキシやトラストストアの設定を渡すには、`-Dspring-boot.run.jvmArguments` を使用します。

**Unix/Linux/Mac（macOS Keychain利用の例）:**

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local \
  -Dspring-boot.run.jvmArguments="-Dhttps.proxyHost=127.0.0.1 \
    -Dhttps.proxyPort=8888 \
    -Djavax.net.ssl.trustStoreType=KeychainStore"
```

**Windows（Windows証明書ストア利用の例）:**

```cmd
mvn spring-boot:run -Dspring-boot.run.profiles=local ^
  -Dspring-boot.run.jvmArguments="-Dhttps.proxyHost=127.0.0.1 ^
    -Dhttps.proxyPort=8888 ^
    -Djavax.net.ssl.trustStoreType=Windows-ROOT"
```

プロキシのホスト・ポートは自身の環境に合わせて変更してください。cacertsにCA証明書をインポート済みの場合は `trustStoreType` の指定は不要です。

## 4. Azure SDK HTTPクライアントについて

本プロジェクトではAzure SDKのデフォルトHTTPクライアントである
`azure-core-http-netty`（Reactor Netty）を除外し、
代わりに `azure-core-http-jdk-httpclient`（JDK標準のHttpClient）を
使用しています。

これにより、企業プロキシ環境で発生しがちなNetty固有のDNS解決エラーやTLS接続の問題を回避できます。JDK標準のHttpClientはJVMシステムプロパティによるプロキシ設定をそのまま利用します。
