#include <MIDIElements.h>

boolean debug = false; // print to serial instead of midi
boolean secondary = true; // enable secondary midi messages
int midiChannel = 1; // midi channel number

int potPin1 = A7;
int potPin2 = A6;
int potPin3 = A0;

int faderPin1 = A20;
int faderPin2 = A19;
int faderPin3 = A18;
int faderPin4 = A17;

int joyPin1 = A16;
int joyPin2 = A15;

int click1Pin = 5;
int click2Pin = 4;
int click3Pin = 3;
int click4Pin = 2;
int click5Pin = 13;

//for capacitive touch buttons
uint8_t touchPin[9] = {23, 22, 17, 16, 15, 33, 32, 0, 1};

//create stored values for cap touch
uint8_t ntouch = 9;
boolean touchState[9] = {false, false, false, false, false, false, false, false, false};
int touchValue = 0;
uint8_t midiNotes[9] = {58, 59, 60, 62, 64, 65, 67, 69, 71}; // C oct5 major scale
elapsedMillis msec = 0;

// declare your components here
MIDIEncoder *enc1;
MIDIEncoder *enc2;
MIDIEncoder *enc3;
MIDIEncoder *enc4;
MIDIEncoder *enc5;

Potentiometer pot1(potPin1, midiChannel, 17, secondary, debug);
Potentiometer pot2(potPin2, midiChannel, 18, secondary, debug);
Potentiometer pot3(potPin3, midiChannel, 19, secondary, debug);

Potentiometer fader1(faderPin1, midiChannel, 20, secondary, debug);
Potentiometer fader2(faderPin2, midiChannel, 21, secondary, debug);
Potentiometer fader3(faderPin3, midiChannel, 22, secondary, debug);
Potentiometer fader4(faderPin3, midiChannel, 23, secondary, debug);

Potentiometer joy1(joyPin1, midiChannel, 24, secondary, debug);
Potentiometer joy2(joyPin2, midiChannel, 25, secondary, debug);

Button clk1(clickPin1, midiChannel, 26, secondary, debug);
Button clk2(clickPin2, midiChannel, 27, secondary, debug);
Button clk3(clickPin3, midiChannel, 28, secondary, debug);
Button clk4(clickPin4, midiChannel, 29, secondary, debug);
Button clk5(clickPin5, midiChannel, 30, secondary, debug);

void setup() {
  
  Serial.begin(38400);
  delay(200);

  enc1 = new MIDIEncoder(25,24,midiChannel,34,true, false);
  enc2 = new MIDIEncoder(12,11,midiChannel,35,true, false);
  enc3 = new MIDIEncoder(10,9,midiChannel,36,true, false);
  enc4 = new MIDIEncoder(8,7,midiChannel,37,true, false);
  enc5 = new MIDIEncoder(6,5,midiChannel,38,true, false);

  usbMIDI.setHandleNoteOff(OnNoteOff); //set event handler for note off
  usbMIDI.setHandleNoteOn(OnNoteOn); //set event handler for note on
  usbMIDI.setHandleControlChange(OnControlChange); // set event handler for CC
}

void loop() {
  //delay(10);
  pot1.read();
  pot2.read();
  pot3.read();

  joy1.read();
  joy2.read();

  fader1.read();
  fader2.read();
  fader3.read();
  fader4.read();
  
  enc1->read();
  enc2->read();
  enc3->read();
  enc4->read();
  enc5->read();
  
  clk1.read();
  clk2.read();
  clk3.read();
  clk4.read();
  clk5.read();

// Scan the Capacitive Touch pins

  for(uint8_t i=0; i < ntouch; i++) {   touchValue = touchRead(touchPin[i]);  // Serial.print("touchPin: ");  // Serial.print(touchPin[i]);  // Serial.print(" ");  // Serial.print("Value: ");  // Serial.println(touchValue);   if(touchValue > 2400 && !touchState[i]) {
    usbMIDI.sendNoteOn(midiNotes[i], 99, 1);
    touchState[i] = 1;
  }
  else if(touchValue < 2400 && touchState[i]) { usbMIDI.sendNoteOff(midiNotes[i], 0, 1); touchState[i] = 0; } 
  }   
  // Below is some code for adding a pot and button as MIDI CCs   if(msec >= 20) {
    msec = 0;
  }

//  while (usbMIDI.read()) {
//
//  }

  usbMIDI.read(); // read all the incoming midi messages
}

// event handlers
void OnNoteOn(byte channel, byte note, byte velocity)
{
  // add all your output component sets that will trigger with note ons
}

void OnNoteOff(byte channel, byte note, byte velocity)
{
  // add all your output component sets that will trigger with note ons
}

void OnControlChange(byte channel, byte control, byte value)
{
  // add all your output component sets that will trigger with cc
}