📲 SMS Forwarder - Android App
===============================

⚡ Overview
-----------
SMS Forwarder is a lightweight Android application designed to securely forward incoming SMS messages to your Telegram bot in real-time.
It supports persistent background operation on modern Android devices (including Xiaomi, Huawei, Vivo) and provides continuous client alive pings
to keep you informed about your device status.

🔥 Features
-----------
- 📩 Instant SMS Forwarding: Receive all SMS messages forwarded automatically to your Telegram chat.
- 🛡️ Persistent Background Service: Runs reliably on Android 12+ devices, survives reboots and battery optimizations.
- ⏰ Heartbeat Pings: Sends periodic "Client Alive" status updates to your Telegram bot.
- 🔒 Secure Telegram Integration: Uses Telegram Bot API to deliver messages securely.
- 🌐 WebView Integration: Loads a Google homepage in-app for user interaction.
- ⚙️ Runtime Permission Management: Handles SMS permissions at runtime for smooth user experience.

🤝 Ethical Use
--------------
This tool is strictly intended for ethical, legal use only — such as:

- Monitoring your own device’s SMS remotely.
- Parental control or enterprise device management.
- Research and learning about Android background services and SMS handling.

Unauthorized interception or forwarding of SMS without consent is illegal and punishable by law.
Use responsibly and at your own risk.

🛠️ Installation & Setup
------------------------
1. Clone or download the project.

   git clone https://github.com/YourUsername/sms-forwarder.git
   cd sms-forwarder

2. Open the project in Android Studio.

3. Replace BOT_TOKEN and CHAT_ID placeholders in SMSReceiver.java and PersistentService.java
   with your Telegram Bot API token and chat ID.

4. Build and run the app on your Android device.

5. Grant SMS permissions when prompted.

6. Your SMS messages will be forwarded to your Telegram chat instantly!

🚀 Usage Tips
-------------
- To test the heartbeat ping interval, check Telegram for periodic "Client Alive" messages.
- To increase/decrease ping frequency, modify the interval in PersistentService.java.
- Add your device to the battery optimization whitelist for uninterrupted service.
- Use Telegram’s bot privacy settings carefully to control who can see forwarded messages.

📚 Learning & Contributions
---------------------------
Feel free to fork and improve this project! Contributions and suggestions are welcome — just open an issue or pull request.

⚠️ Disclaimer
-------------
This application is provided “as-is” for educational and personal use. The author is not responsible for any misuse or legal consequences resulting from this software.


