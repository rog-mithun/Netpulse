package netpulse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.regex.Pattern;
import java.util.zip.CRC32;


public class server {
    private static final int SERVER_PORT = 12345;
    private static final int PACKET_SIZE = 1024;
    public static void main(String[] args) {
        try {
            // Create a server socket
            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);
            System.out.println("Server started and listening on port " + SERVER_PORT);

            while (true) {
                // Accept client connection
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected: " + clientSocket.getInetAddress());

                // Create a thread to handle the client request
                Thread clientThread = new Thread(() -> handleClient(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            // Create a BufferedReader to read input from the client
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            // Create a PrintWriter to send output to the client
            PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);

            // Read the website name from the client
            String websiteName = reader.readLine();

            // Perform network analysis for the website
            NetworkAnalysisResult result = analyzeNetworkPerformance(websiteName);

            // Send the analysis result to the client
            writer.println(result.getFormattedResult());

            // Print the analysis result
            System.out.println(result.getFormattedResult());

            // Close the client socket
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static NetworkAnalysisResult analyzeNetworkPerformance(String websiteName) {
    if (!isValidWebsite(websiteName)) {
        return new NetworkAnalysisResult(websiteName, "Invalid Website", 0, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    try {
        InetAddress websiteAddress = InetAddress.getByName(websiteName);

        int dataPackets = measureDataPackets(websiteAddress);
        int framesPassed = measureFramesPassed();
        long latency = measureLatency(websiteAddress);
        double jitter = measureJitter(websiteAddress);
        double bandwidthUtilization = measureBandwidthUtilization();
        double packetLoss = measurePacketLoss(websiteAddress);
        String tcpFlags = measureTCPFlags(websiteAddress);
        long checksum = calculateChecksum(websiteName);
        int totalBitsPassed = calculateTotalBitsPassed(dataPackets, PACKET_SIZE);
        int bitsPassedPerPacket = calculateBitsPassedPerPacket(totalBitsPassed, dataPackets);
        String resultMessage;
        if (tcpFlags == null) {
            resultMessage = "Failed to retrieve TCP flags";
        } else {
            resultMessage = "Success (" + tcpFlags +" )";
        }

        return new NetworkAnalysisResult(websiteName, resultMessage, dataPackets, framesPassed, latency, jitter,
                bandwidthUtilization, packetLoss, checksum, totalBitsPassed, bitsPassedPerPacket);
    } catch (IOException e) {
        e.printStackTrace();
    }

    return new NetworkAnalysisResult(websiteName, "Unknown Error", 0, 0, 0, 0, 0, 0, 0, 0, 0);
}

    private static String measureTCPFlags(InetAddress websiteAddress) {
    try (Socket socket = new Socket(websiteAddress, 80)) {
        socket.setSoTimeout(2000); // Set socket timeout to 2000 milliseconds

        // Get the TCP flags from the socket
        boolean urgentData = socket.getOOBInline();
        boolean keepAlive = socket.getKeepAlive();
        boolean noDelay = socket.getTcpNoDelay();
        boolean reuseAddress = socket.getReuseAddress();

        StringBuilder flagsBuilder = new StringBuilder();
        if (urgentData) {
            flagsBuilder.append("URG ");
        }
        if (keepAlive) {
            flagsBuilder.append("ACK ");
        }
        if (noDelay) {
            flagsBuilder.append("PSH ");
        }
        if (reuseAddress) {
            flagsBuilder.append("RST ");
        }

        // Check if any flags were set
        if (flagsBuilder.length() == 0) {
            return "Flags Passed";
        } else {
            return flagsBuilder.toString().trim();
        }
    } catch (IOException e) {
        e.printStackTrace();
        return null; // Return null if there is an error
    }
}

    private static int measureDataPackets(InetAddress websiteAddress) throws IOException {
    int dataPackets = 0;
    int totalBitsPassed = 0;
    try {
        URL url = new URL("http://" + websiteAddress.getHostAddress());
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(2000); // Set connection timeout to 2000 milliseconds
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            // Read the response from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                // Keep reading until the response is fully received
                dataPackets++;
                if (line != null) {
                totalBitsPassed += line.length() * 8;
            }
           }      
        } else {
            System.out.println("Error: HTTP response code - " + responseCode);
            // You can add further error handling or logging here
        }

        connection.disconnect();
    } catch (IOException e) {
        e.printStackTrace();
    }
    
    return dataPackets;
}


private static int measureFramesPassed() {
    int framesPassed = 0;
    DatagramSocket socket = null;
    try {
        socket = new DatagramSocket();
        socket.setSoTimeout(1000); // Set a timeout to prevent indefinite waiting

        byte[] buffer = new byte[1024];

        // Send a dummy packet to trigger capturing
        InetAddress localAddress = InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length, localAddress, socket.getLocalPort());
        socket.send(packet);

        // Receive packets until timeout occurs
        while (true) {
            try {
                packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                framesPassed++;
            } catch (SocketTimeoutException e) {
                // Timeout occurred, stop capturing
                break;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        if (socket != null) {
            socket.close();
        }
    }

    return framesPassed;
}

    private static long measureLatency(InetAddress websiteAddress) throws IOException {
    long latency = 0;

    // Send ICMP echo request (ping) to the website
    long startTime = System.currentTimeMillis();
    if (websiteAddress.isReachable(3000)) { // Set a timeout of 3000 milliseconds
        long endTime = System.currentTimeMillis();
        latency = endTime - startTime;
    } else {
        // Failed to reach the website, assign a large latency value
        latency = Long.MAX_VALUE;
    }

    return latency;
}
    private static double measureJitter(InetAddress websiteAddress) throws IOException {
    int numPings = 10; // Number of ICMP echo requests to send
    long[] roundTripTimes = new long[numPings]; // Array to store round-trip times

    // Send ICMP echo requests (pings) to the website
    for (int i = 0; i < numPings; i++) {
        long startTime = System.currentTimeMillis();
        if (websiteAddress.isReachable(3000)) { // Set a timeout of 3000 milliseconds
            long endTime = System.currentTimeMillis();
            roundTripTimes[i] = endTime - startTime;
        } else {
            // Failed to reach the website, assign a large round-trip time
            roundTripTimes[i] = Long.MAX_VALUE;
        }
    }

    // Calculate the variation in round-trip times (jitter)
    double sumOfDifferences = 0;
    for (int i = 0; i < numPings - 1; i++) {
        long difference = roundTripTimes[i + 1] - roundTripTimes[i];
        sumOfDifferences += Math.abs(difference);
    }

    double jitter = sumOfDifferences / (numPings - 1);
    return jitter;
}


  private static double measureBandwidthUtilization() {
    final int bufferSize = 8192; // Adjust the buffer size as needed
    final int durationSeconds = 5; // Duration of measurement in seconds
    final int packetSizeBytes = 1024; // Define the packet size in bytes

    try (DatagramSocket socket = new DatagramSocket()) {
        socket.setSoTimeout(1000);

        byte[] buffer = new byte[bufferSize];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (durationSeconds * 1000);

        int totalPacketsReceived = 0;

        while (System.currentTimeMillis() < endTime) {
            try {
                socket.receive(packet);
            } catch (SocketTimeoutException ignored) {
            }

            totalPacketsReceived++;
        }

        double packetRate = totalPacketsReceived / (double) durationSeconds;
        double maximumBandwidthMbps = 1000.0; // Specify the maximum available bandwidth in Mbps

        double bandwidthUtilization = (packetRate * packetSizeBytes * 8.0) / (maximumBandwidthMbps * 1000000) * 100.0;

        return bandwidthUtilization;
    } catch (IOException e) {
        e.printStackTrace();
        return 0.0; // Return 0 if there is an error
    }
}
  private static double measurePacketLoss(InetAddress websiteAddress) throws IOException {
    final int numPacketsToSend = 100; // Number of packets to send
    final int timeoutMillis = 1000; // Timeout for receiving each packet in milliseconds

    // Create a DatagramSocket to send and receive packets
    try (DatagramSocket socket = new DatagramSocket()) {
        socket.setSoTimeout(timeoutMillis);

        // Prepare the packet data
        byte[] packetData = "Ping".getBytes();

        int packetsSent = 0;
        int packetsLost = 0;

        // Send and receive packets
        while (packetsSent < numPacketsToSend) {
            // Create a DatagramPacket to send
            DatagramPacket sendPacket = new DatagramPacket(packetData, packetData.length, websiteAddress, 80);

            // Send the packet
            socket.send(sendPacket);
            packetsSent++;

            // Create a DatagramPacket to receive the response
            DatagramPacket receivePacket = new DatagramPacket(new byte[packetData.length], packetData.length);

            try {
                // Receive the response packet
                socket.receive(receivePacket);
            } catch (SocketTimeoutException e) {
                // Packet loss occurred (no response received)
                packetsLost++;
            }
        }

        // Calculate the packet loss percentage
        double packetLossPercentage = 0.0;
        if (packetsLost > 0) {
            packetLossPercentage = (packetsLost / (double) packetsSent) * 100.0;
        }

        return packetLossPercentage;
    } catch (IOException e) {
        e.printStackTrace();
        return 0.0; // Return 0 if there is an error
    }
}
    private static boolean isValidWebsite(String websiteName) {
        // Use a regular expression to validate the website name
        // Here, we check if the website name is in the www.domain.com format
        String regex = "^www\\.[a-zA-Z0-9-]+(\\.[a-zA-Z]{2,})+$";
        return Pattern.matches(regex, websiteName);
    }
    private static long calculateChecksum(String data) {
        CRC32 checksum = new CRC32();
        checksum.update(data.getBytes());
        return checksum.getValue();
    }
     private static int calculateTotalBitsPassed(int dataPackets, int packetSize) {
        return dataPackets * packetSize * 8;
    }

    private static int calculateBitsPassedPerPacket(int totalBitsPassed, int dataPackets) {
        if (dataPackets > 0) {
            return totalBitsPassed / dataPackets;
        } else {
            return 0;
        }
}
}
