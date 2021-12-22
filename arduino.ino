#include <SoftwareSerial.h>
#include "DHT.h"
#define DHTPIN 2     // what digital pin we're connected to
#define DHTTYPE DHT22   
DHT dht(DHTPIN, DHTTYPE);

SoftwareSerial Bluetooth(10, 11);
long int donnees; // stocke la valeur envoyé via le gsm

int pin8 = 8;
int capteuranalogique = A1;
int capteurnumerique = A0;
int sensorValue1 = 0;
int sensorValue2 = 0;
int maxi = 1023; // pour avoir toute la fenetre du traceur série 
int mini = 0; // pour avoir toute la fenetre du traceur serie
int pin2 = 2;
int vhum = 0;
int vtemp = 0;

long int pwd1 = 92;// light on
long int pwd2 = 79; // light off
char state = 0;

void setup() {
pinMode(pin8, OUTPUT); // broche d'alimentation du relais
pinMode(4, OUTPUT); // broche d'alimentation de la sonde
pinMode(pin2, OUTPUT);
Serial.begin(9600);
Bluetooth.begin(9600);
digitalWrite(pin8, LOW);
digitalWrite(4, LOW); // met la sonde hors tension - protection oxydation
dht.begin();

}


void loop() {
vhum = analogRead(4);
vtemp =analogRead(pin2);


Bluetooth.print("L'humidité du sol est de :");
Bluetooth.println(vhum);
Bluetooth.println("************************");
Bluetooth.println("La tempérture de la piece est:");
Bluetooth.println("vtemp");
Bluetooth.println("************************");
while(Bluetooth.available()==0) ;
 
 if(Bluetooth.available()>0) // si le bluetooth reçois une valeur supérieur à zéro
{
donnees = Bluetooth.parseInt();
 
} 

delay(100);

Serial.print(donnees);
 
if(donnees == pwd1)
{
  
  digitalWrite(4,HIGH);
  Serial.println("Arrosage activé");/********************************************* affiché plt grace au bth 'debut de l'arrosage'*/

Serial.println(" SOL PAS ASSEZ HUMIDE - ACTIVATION ARROSAGE ");
  for(int arrosage = 1 ; arrosage <= 10 ; arrosage = arrosage+1) // cycle maxi de 10 tests + arrosage pour permettre que l'eau ait le temps de s'infiltrer dans le sol
     {
     sensorValue1 = analogRead(capteuranalogique); 
     if (sensorValue1 > 90) {
        Serial.print(arrosage);
        Serial.print(" arrosage ");
        Serial.print(" ; ");
        Serial.println(sensorValue1);
        digitalWrite(pin8, HIGH); 
        delay(2000); // durée fixe activation pompe
        digitalWrite(pin8, LOW); 
        delay(5000); // pause pompe pour laisser a l'eau le tps de pénétrer la terre  
        }
      
     }
 }
 
if(donnees == pwd2)
  {

    digitalWrite(pin8, LOW);
    digitalWrite(4, HIGH);
    Serial.println("fin de l'arrosage");

  }
 

digitalWrite(4, LOW);// remet la sonde hors tension pendant temporisation pour protection oxydation  
for(int attente= 0; attente <= 8640 ; attente = attente+1) // mettre 8640 !
{
  delay(10); // cette boucle for protège la sonde car ne fait qu'une mesure par 24h
}

digitalWrite(4, HIGH); // met la sonde sous tension

delay(10);
sensorValue1 = analogRead(capteuranalogique); // mesure humidité
sensorValue2 = analogRead(capteurnumerique);

Serial.print("Temperature de la piece: ");
  
Serial.print(getTemp("c"));
Serial.print(" *C ");
Serial.print("Humidité de la piece: ");
Serial.print(getTemp("h"));
Serial.println(" % ");

Serial.print(maxi);
Serial.print(" ; ");
Serial.print(mini);
Serial.print("  ;  VALEUR ANALOGIQUE = ");
Serial.print(sensorValue1);
Serial.print("  ; VALEUR NUMERIQUE = " );
Serial.println(sensorValue2);

// si sensorValue1 est supérieur au seuil sec

  
}


float getTemp(String req)
{

  
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  float hic = dht.computeHeatIndex(t, h, false);
  if (isnan(h) || isnan(t) ) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }
  
  float k = t + 273.15;
  if(req =="c"){
    return t;//retourne la temperature en Cilsus
  }else if(req =="h"){
    return h;// retourn l'humidité
  }else if(req =="hic"){
    return hic;
  }else{
    return 0.000;// si pas de demande , retourne 0.000
  }
 
}
