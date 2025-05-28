#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>

// --- Credenciais do WiFi ---
#define WIFI_SSID "igru"
#define WIFI_PASSWORD "senhadificil"

// --- Credenciais do Firebase ---
#define FIREBASE_HOST "https://projetos9-8b610-default-rtdb.firebaseio.com/" 
#define FIREBASE_AUTH "vFnruXsqY3BWeeDzXBVaNvddWp2TCktBei1aFiB5"

FirebaseData firebaseData;
FirebaseAuth auth;
FirebaseConfig config;

void setup() {
  Serial.begin(115200);
  
  // Conecta ao WiFi
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Conectando ao WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("Conectado ao WiFi");

  // Configura o Firebase
  config.host = FIREBASE_HOST;
  config.signer.tokens.legacy_token = FIREBASE_AUTH;

  // Inicializa a conexão com o Firebase
  Firebase.begin(&config, &auth);

  // (Opcional) Define reconexão automática ao WiFi
  Firebase.reconnectWiFi(true);
}

void loop() {
  // Exemplo: grava um valor
  if (Firebase.setInt(firebaseData, "/teste", 123)) {
    Serial.println("Dados enviados!");
  } else {
    Serial.print("Erro: ");
    Serial.println(firebaseData.errorReason());
  }

  delay(5000);
}

