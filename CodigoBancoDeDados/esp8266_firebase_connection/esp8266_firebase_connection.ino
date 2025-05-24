#include <ESP8266WiFi.h>
#include <FirebaseESP8266.h>

// --- Credenciais do WiFi ---
#define WIFI_SSID "SUA_REDE_WIFI"
#define WIFI_PASSWORD "SUA_SENHA_WIFI"

// --- Credenciais do Firebase ---
#define FIREBASE_HOST "PI9.firebaseio.com" 
#define FIREBASE_AUTH "vFnruXsqY3BWeeDzXBVaNvddWp2TCktBei1aFiB5"

// --- Objetos Firebase ---
FirebaseData firebaseData;

// --- Variável de exemplo para enviar ---
int contador = 0;

void setup() {
  Serial.begin(115200);
  Serial.println();
  Serial.println("Iniciando...");

  // --- Conectar ao WiFi ---
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Conectando ao WiFi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("Conectado com sucesso! Endereço IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  // --- Inicializar Firebase ---
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);
  Firebase.reconnectWiFi(true);

  Serial.println("Setup concluído.");
}

void loop() {
  
  String caminhoFirebase = "/esp8266/contador";

  Serial.print("Enviando contador para Firebase: ");
  Serial.println(contador);

  // Envia o valor do contador para o Firebase no caminho especificado
  if (Firebase.setInt(firebaseData, caminhoFirebase, contador)) {
    Serial.println("Dados enviados com sucesso!");
  } else {
    Serial.print("Erro ao enviar dados: ");
    Serial.println(firebaseData.errorReason());
  }

  contador++; // Incrementa o contador

  delay(5000); // Espera 5 segundos antes de enviar novamente
}

