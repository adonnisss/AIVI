package org.example;

import com.evplatform.service.interfaces.ChargingProcessorRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Izberite odjemalca za testiranje:");
        System.out.println("1. Oddaljeni EJB odjemalec (Naloga 8)");
        System.out.println("2. JAX-RS REST odjemalec (Naloga 10)");

        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        if (choice == 1) {
            testEJBClient();
        } else if (choice == 2) {
            testRESTClient();
        } else {
            System.out.println("Napačna izbira.");
        }
    }

    /**
     * Ta metoda implementira zahteve iz naloge 8:
     * Pripravi oddaljenega odjemalca, ki preveri, ali lahko vozilo
     * napolnimo na izbrani polnilni postaji.
     */
    public static void testEJBClient() {
        try {
            // 1. Povezava in iskanje EJB zrna
            ChargingProcessorRemote processor = lookupChargingProcessor();
            System.out.println("✅ USPEH: EJB odjemalec uspešno povezan s strežnikom.");

            // 2. Priprava na interaktivni vnos
            Scanner scanner = new Scanner(System.in);
            System.out.println("\n--- Testiranje preverjanja možnosti polnjenja ---");
            System.out.println("Vnesite ID uporabnika (npr. 1 za John Doe):");
            int userId = scanner.nextInt();
            System.out.println("Vnesite ID polnilne postaje (npr. 1 za ElectroDrive City Center):");
            int stationId = scanner.nextInt();
            System.out.println("Vnesite predvideno ceno polnjenja (npr. 20.5):");
            double cost = scanner.nextDouble();

            // 3. Klic oddaljene metode na strežniku
            System.out.println("\nCLIENT: Pošiljam zahtevek na strežnik...");
            boolean canCharge = processor.processChargingRequest(userId, stationId, cost);

            // 4. Izpis rezultata, prejetega od strežnika
            System.out.println("\nCLIENT: Prejet odgovor od strežnika.");
            System.out.println("======================================");
            if (canCharge) {
                System.out.println("REZULTAT: Zahteva ODOBRENA. Polnjenje se lahko prične.");
            } else {
                System.out.println("REZULTAT: Zahteva ZAVRNJENA. Preverite konzolo strežnika za razlog.");
            }
            System.out.println("======================================");

        } catch (NamingException e) {
            System.err.println("❌ NAPAKA: EJB zrna ni mogoče najti. Preverite JNDI ime in ali je aplikacija na strežniku uspešno nameščena.");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Prišlo je do nepričakovane napake:");
            e.printStackTrace();
        }
    }

    /**
     * Ta metoda implementira zahteve iz naloge 10:
     * Pripravi zunanjo aplikacijo kot odjemalca REST storitve.
     * Testira GET in POST zahteve za uporabnike in postaje.
     */
    public static void testRESTClient() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String baseUri = "http://localhost:8080/ev-platform-jsf/api";

        // --- TEST 1: GET VSI UPORABNIKI ---
        System.out.println("\n--- TEST 1: Pridobivanje vseh uporabnikov (GET /users) ---");
        HttpRequest getUsersRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/users"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> usersResponse = client.send(getUsersRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + usersResponse.statusCode());
        System.out.println("Odgovor: " + usersResponse.body());

        // --- TEST 2: GET VSE POSTAJE ---
        System.out.println("\n--- TEST 2: Pridobivanje vseh postaj (GET /stations) ---");
        HttpRequest getStationsRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/stations"))
                .header("Accept", "application/json")
                .build();
        HttpResponse<String> stationsResponse = client.send(getStationsRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + stationsResponse.statusCode());
        System.out.println("Odgovor: " + stationsResponse.body());

        // --- TEST 3: DODAJANJE NOVEGA UPORABNIKA ---
        System.out.println("\n--- TEST 3: Dodajanje novega uporabnika (POST /users) ---");
        String newUserJson = """
                {
                  "name": "REST Client User",
                  "email": "rest.client@example.com",
                  "balance": 150.0,
                  "carType": "TRUCK"
                }
                """;
        HttpRequest postUserRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(newUserJson))
                .build();
        HttpResponse<String> postUserResponse = client.send(postUserRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + postUserResponse.statusCode());
        System.out.println("Odgovor: " + postUserResponse.body());


        // --- TEST 4: DODAJANJE NOVE POSTAJE ---
        System.out.println("\n--- TEST 4: Dodajanje nove postaje (POST /stations) ---");
        String newStationJson = """
                {
                  "name": "Postaja od REST Odjemalca",
                  "location": "Java Avenue",
                  "coordinates": "46.0, 14.5",
                  "status": "AVAILABLE",
                  "numberOfConnectors": 4,
                  "maxPowerKw": 150.0,
                  "providerId": 2
                }
                """;
        HttpRequest postStationRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUri + "/stations"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(newStationJson))
                .build();
        HttpResponse<String> postStationResponse = client.send(postStationRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("Status: " + postStationResponse.statusCode());
        System.out.println("Odgovor: " + postStationResponse.body());
    }

    private static ChargingProcessorRemote lookupChargingProcessor() throws NamingException {
        final Properties jndiProperties = new Properties();
        jndiProperties.put(Context.INITIAL_CONTEXT_FACTORY, "org.wildfly.naming.client.WildFlyInitialContextFactory");
        jndiProperties.put(Context.PROVIDER_URL, "http-remoting://localhost:8080");
        jndiProperties.put(Context.SECURITY_PRINCIPAL, "testuser");
        jndiProperties.put(Context.SECURITY_CREDENTIALS, "testpassword1!");
        final Context context = new InitialContext(jndiProperties);
        final String jndiName = "ev-platform-jsf/ChargingRequestProcessor!com.evplatform.service.interfaces.ChargingProcessorRemote";
        return (ChargingProcessorRemote) context.lookup(jndiName);
    }
}
