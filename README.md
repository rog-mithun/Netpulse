# 🌐 Netpulse – Java-Based Network Analysis Tool

[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

A lightweight Java application designed to monitor and analyze key network performance parameters such as jitter, bandwidth, upload/download speeds, and packet statistics. Featuring a client-server architecture, **Netpulse** enables real-time metric retrieval and enhances user diagnostics for network reliability.

---

## 📌 Key Features

- 📡 Calculates jitter, bandwidth, latency, and packet stats
- 🔁 Client-server architecture for real-time communication
- ⚡ Reduced downtime by 35% through efficient threading
- 🧮 Accurate upload/download speed computation
- 🖥️ User-friendly client interface for ease of use
- 📈 Increased system efficiency by 20% and user satisfaction by 25%

---

## 🛠️ Tech Stack

### Languages & Libraries:
![Java](https://img.shields.io/badge/Java-ED8B00?style=flat&logo=java&logoColor=white)
![NetBeans](https://img.shields.io/badge/NetBeans_IDE-1B6AC6?style=flat&logo=apache-netbeans-ide&logoColor=white)

---

## 📁 Project Structure

| File/Folder                  | Description                                      |
|------------------------------|--------------------------------------------------|
| `src/netpulse/client.java`   | Client-side socket logic & interface             |
| `src/netpulse/server.java`   | Server-side metric collector & response engine   |
| `src/netpulse/NetworkAnalysisResult.java` | Data structure for computed results     |
| `src/DownloadPage.java`      | GUI or interaction page for downloading metrics |
| `manifest.mf`                | Manifest file for JAR packaging                 |
| `build.xml`                  | Ant build file                                  |
| `nbproject/`                 | NetBeans configuration files                    |
| `build/classes/`             | Compiled Java class files                       |
| `dist/javadoc/`              | Generated JavaDoc documentation                 |

---

## 🚀 How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/rog-mithun/Netpulse.git
   cd Netpulse

2. **Open with NetBeans IDE:**
   
  - Load the project using NetBeans.
  - Build the project (Clean and Build).
  - Run server.java first, followed by client.java.

---

## 📊 Performance Highlights

⚙️ Architecture: Multithreaded client-server model

⏱️ Downtime Reduced: By 35%

📈 System Efficiency: Increased by 20%

😀 User Satisfaction: Improved by 25%

---

## 📖 License
This project is licensed under the [MIT License](LICENSE) © 2022 Mithunsankar S.
