package netpulse;

public class NetworkAnalysisResult {
    private String websiteName;
    private String resultMessage;
    private String flag;
    private int dataPackets;
    private int framesPassed;
    private long latency;
    private double jitter;
    private double bandwidthUtilization;
    private double packetLoss;
    private long checksum;
    private int totalBitsPassed;
    private int bitsPassedPerPacket;

    public NetworkAnalysisResult(String websiteName, String flag, int dataPackets, int framesPassed, long latency,
            double jitter, double bandwidthUtilization, double packetLoss, long checksum, int totalBitsPassed, int bitsPassedPerPacket) {
        this.websiteName = websiteName;
        this.flag = flag;
        this.dataPackets = dataPackets;
        this.framesPassed = framesPassed;
        this.latency = latency;
        this.jitter = jitter;
        this.bandwidthUtilization = bandwidthUtilization;
        this.packetLoss = packetLoss;
        this.checksum = checksum;
        this.totalBitsPassed = totalBitsPassed;
        this.bitsPassedPerPacket = bitsPassedPerPacket;

    }

    public String getFormattedResult() {
        StringBuilder sb = new StringBuilder();
        sb.append("Website: ").append(websiteName).append("\n");
        sb.append("Flag: ").append(flag).append("\n");
        sb.append("Number of Data Packets: ").append(dataPackets).append("\n");
        sb.append("Number of Frames Passed: ").append(framesPassed).append("\n");
        sb.append("Network Latency: ").append(latency).append(" ms").append("\n");
        sb.append("Jitter: ").append(jitter).append("\n");
        sb.append("Bandwidth Utilization: ").append(bandwidthUtilization).append("%").append("\n");
        sb.append("Packet Loss: ").append(packetLoss).append("%").append("\n");
        sb.append("Checksum: ").append(checksum).append("\n");
        sb.append("Total Bits Passed: ").append(totalBitsPassed).append("\n");
        sb.append("Bits Per Data Packet: ").append(bitsPassedPerPacket).append("\n");

        return sb.toString();
    }
}
