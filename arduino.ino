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

long int pwd1 = 1;//  on
long int pwd2 = 0; // off
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
vhum = analogRead(capteuranalogique);
vtemp =analogRead(DHTPIN);

while(Bluetooth.available()== 0){
    Bluetooth.print("L'humidité du sol est de :");
    Bluetooth.println(vhum);
    Bluetooth.println("************************");
    Bluetooth.println("La tempérture de la piece est:");
    Bluetooth.println(getTemp("c"));
    Bluetooth.println("************************");
    Bluetooth.println("L'humidité de la pice est");
    Bluetooth.println(getTemp("h"));
    Bluetooth.println("************************");
}
while(Bluetooth.available()==0) ;
 
 if(Bluetooth.available()>0) // si le bluetooth reçois une valeur supérieur à zéro
{
donnees = Bluetooth.parseInt();
 
} 

delay(100);

Serial.println(donnees);
 
if(donnees == pwd1)
{
  
  digitalWrite(4,HIGH);
  Serial.println("Arrosage activé");/********************************************* affiché plt grace au bth 'debut de l'arrosage'*/

Bluetooth.println(" SOL PAS ASSEZ HUMIDE - ACTIVATION ARROSAGE ");
Bluetooth.println("Debut de l'arrosage");
  for(int arrosage = 1 ; arrosage <= 10 ; arrosage = arrosage+1) // cycle maxi de 10 tests + arrosage pour permettre que l'eau ait le temps de s'infiltrer dans le sol
     {
     sensorValue1 = analogRead(capteuranalogique); 
     if (sensorValue1 > 90) {
        Bluetooth.print(arrosage);
        Bluetooth.print(" arrosage ");
        Bluetooth.print(" ; ");
        Bluetooth.println(sensorValue1);
        digitalWrite(pin8, HIGH); 
        delay(500); // durée fixe activation pompe
        digitalWrite(pin8, LOW); 
        delay(5000); // pause pompe pour laisser a l'eau le tps de pénétrer la terre  
        }
      
     }
 }
 
if(donnees == pwd2)
  {

    digitalWrite(pin8, LOW);
    digitalWrite(4, LOW);
    Serial.println("fin de l'arrosage");
    Bluetooth.println("Debut de l'arrosage");

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
Bluetooth.print("**********************");
Bluetooth.println("Temperature de la piece: ");
Bluetooth.print(getTemp("c"));
Bluetooth.print(" *C ");
Bluetooth.println("**********************");
Bluetooth.println("Humidité de la piece: ");
Bluetooth.print(getTemp("h"));
Bluetooth.print(" % ");
Bluetooth.println("**********************");

Bluetooth.println("  ;  VALEUR ANALOGIQUE = ");
Bluetooth.print(sensorValue1);
Bluetooth.println("**********************");
Bluetooth.println("  ; VALEUR NUMERIQUE = " );
Bluetooth.println(sensorValue2);
Bluetooth.println("**********************");

// si sensorValue1 est supérieur au seuil sec

  
}


float getTemp(String req)
{

  
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  float hic = dht.computeHeatIndex(t, h, false);
  if (isnan(h) || isnan(t) ) {
    Serial.println("Erreur de lecture du capteur DHT!");
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
