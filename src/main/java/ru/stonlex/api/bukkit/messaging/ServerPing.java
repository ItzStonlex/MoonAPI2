package ru.stonlex.api.bukkit.messaging;

import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;

@Getter
@Setter
class ServerPing {

    /**
     * Этот код я вообще взял с интернета, поэтому он неоч,
     * но сам по себе очень даже юзабельный
     */

    private String name;
    private String address;
    private String gameVersion;
    private String motd;

    private int port;
    private int pingVersion;
    private int protocolVersion;
    private int playersOnline;
    private int maxPlayers;

    private boolean enabled;

    ServerPing(String address, int port) {
        this.motd = "Offline";

        this.pingVersion = -1;
        this.protocolVersion = -1;
        this.playersOnline = -1;
        this.maxPlayers = -1;

        this.address = address;
        this.port = port;

        this.enabled = this.fetchData();
    }

    private boolean fetchData() {
        try {
            Socket socket = new Socket();

            socket.setSoTimeout(2);
            socket.connect(new InetSocketAddress(this.getAddress(), this.getPort()), 10);

            final OutputStream outputStream = socket.getOutputStream();
            final DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            final InputStream inputStream = socket.getInputStream();
            final InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));

            dataOutputStream.write(new byte[]{-2, 1});

            final int packetId = inputStream.read();

            if (packetId == -1) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                socket = null;
                return false;
            }

            if (packetId != 255) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                socket = null;
                return false;
            }

            final int length = inputStreamReader.read();

            if (length == -1) {
                try {
                    socket.close();
                } catch (IOException ex3) {
                }
                socket = null;
                return false;
            }

            if (length == 0) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                socket = null;
                return false;
            }

            final char[] chars = new char[length];

            if (inputStreamReader.read(chars, 0, length) != length) {
                try {
                    socket.close();
                } catch (IOException ignored) {
                }
                socket = null;
                return false;
            }

            final String string = new String(chars);

            if (string.startsWith("§")) {
                final String[] data = string.split("\u0000");

                this.setPingVersion(Integer.parseInt(data[0].substring(1)));
                this.setProtocolVersion(Integer.parseInt(data[1]));
                this.setGameVersion(data[2]);
                this.setMotd(data[3]);
                this.setPlayersOnline(Integer.parseInt(data[4]));
                this.setMaxPlayers(Integer.parseInt(data[5]));
            } else {
                final String[] data = string.split("§");

                this.setMotd(data[0]);
                this.setPlayersOnline(Integer.parseInt(data[1]));
                this.setMaxPlayers(Integer.parseInt(data[2]));
            }

            dataOutputStream.close();

            outputStream.close();

            inputStreamReader.close();

            inputStream.close();

            socket.close();
        } catch (IOException exception) {
            return false;
        }
        return true;
    }

}
