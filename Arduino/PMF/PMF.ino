#include "DHT.h"
#include "Adafruit_Sensor.h"

#define DHTPIN 2 //Pin du DHT22 
#define DHTTYPE DHT22
#define THERMIPIN 0 //Pin de la thermistance 
#define MOSFETPIN 7 //Pin du transistor MOSFET 

DHT dht (DHTPIN, DHTTYPE); //Déclaration du capteur

//DHT22
float dht_humi; //Humidité mesurée par le DHT22
float dht_temp; //Température mesurée par le DHT22 (température frigo)
//THERMISTANCE
float thermi_resi; //Résistance de la thermistance
float thermi_temp; //Température mesurée par la thermistance (température ambiante)
float thermi_tens; //Tension aux bornes de la thermistance
//PONT DIVISEUR
float pontdiv_resi = 10000.0; //Résistance du pont diviseur
//COEFFICIENT DE STEINHARTHART<
float steinharthart_coefA = 0.00088386; //Coefficient de SteinHartHart A à 0°c
float steinharthart_coefB = 0.0002583; //Coefficient de SteinHartHart B à 25°c
float steinharthart_coefC = 0.00000011661; //Coefficient de SteinHartHart C à 100°c
//CIRCUIT
float circ_tens_in = 5; //Tension du circuit
//POINT DE ROSÉE
float pt_rosee_k; //Point de rosée en Kelvin
float pt_rosee_c; //Point de rosée en degrés Celsius
float k_rosee; //Constante K du point de Rosée
boolean condensation = 0; //Condensation dans le frigo
//Consigne
float consigne;
int count = 0;

//___________________________________________________________________________________________
void setup()
{
  Serial.begin(9600);
  pinMode(DHTPIN, INPUT);
  pinMode(MOSFETPIN, OUTPUT);
  dht.begin();
} //End setup

//___________________________________________________________________________________________
void regulateur()
{
  if (dht_temp < consigne)
  {
    regul_high();
  }
  else
  {
    regul_low();
  }
} //End regulateur
//___________________________________________________________________________________________
void regul_high()
{
  if (dht_temp < consigne + 1)
  {
    digitalWrite(7, HIGH);
    //Serial.println("Refroidissement actif");
  }
} //End regul_high

//___________________________________________________________________________________________
void regul_low()
{
  if (dht_temp > consigne - 1)
  {
    digitalWrite(7, LOW);
    //Serial.println("Refroidissement inactif");
  }
} //End regul_low

//___________________________________________________________________________________________
void sup_rosee()
{
  if (dht_temp >= (pt_rosee_c * 1.25))
  {
    //Serial.println("ATTENTION CONDENSATION");
  }
} //End sup_rossee

//___________________________________________________________________________________________
void inf_rosee()
{
  if (dht_temp <= (pt_rosee_c * 0.75))
  {
    //Serial.println("ATTENTION CONDENSATION");
  }
}//End inf_rosee

//___________________________________________________________________________________________

void loop()
{
  //CAPTEURS
  thermi_tens = analogRead(THERMIPIN) * 5.0 / 1024.0; //Calcul de la tension depuis le DHT22

  thermi_resi = (thermi_tens * pontdiv_resi) / (circ_tens_in - thermi_tens);

  thermi_temp = (1.0 / (steinharthart_coefA + steinharthart_coefB * log(thermi_resi) + steinharthart_coefC * pow(log(thermi_resi), 3))) - 273.15;
  //Calcul de la température de la thermistanc

  float dht_humi = dht.readHumidity(); //Lecture du taux d'humidité sur le DHT22

  float dht_temp = dht.readTemperature(); //Lecture de la température sur le DHT22

  float k_rosee = (((237.7) * dht_temp) / (17.7 + dht_temp)) + (log(dht_humi));

  float pt_rosee_k = (((237.7) * k_rosee) / (17.7 - k_rosee));

  float pt_rosee_c = pt_rosee_k + 273.15;

  boolean condensation = 0;

  //AFFICHAGE
  /*sprintf(input,"SS:%f:%f:%f", //Trame pour regrouper les informations
        thermi_temp,
        dht_temp,
        dht_humi
         );*/


  //Calcul et affichage de la tension de la thermistance
  Serial.println(thermi_tens);

  //Calcul et affichage de la résistance de la thermistance
  Serial.println(thermi_resi);

  Serial.println(thermi_temp);

  Serial.println(dht_humi);

  Serial.println(dht_temp);



  Serial.println(pt_rosee_c);

  count ++;
  Serial.println(count);

  delay(1000);
  regulateur();

  if (dht_temp > pt_rosee_c) {
    sup_rosee();
  }
  else {
    inf_rosee();
  }

} //End Loop

void serialEvent(){
  String input = Serial.readString();
  consigne = input.toFloat();
}


