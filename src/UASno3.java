import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UASno3 {

        private int id;
        private String title;
        private double rating;

        public UASno3(int id, String title, double rating) {
            this.id = id;
            this.title = title;
            this.rating = rating;
        }
        public int getId() {
            return id;
        }
        public String gettitle() {
            return title;
        }
        public double getRating() {
            return rating;
        }

        public static void main(String[] args) {
            String url = "https://dummyjson.com/products";
            String consld = "1234567";
            String userKey = "faY738sH";

            try {
                URL apiURL = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) apiURL.openConnection();
                connection.setRequestMethod("GET");

                connection.setRequestProperty("X-Cons-ID", consld);
                connection.setRequestProperty("User-Key", userKey);

                //menerima response JSON
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) !=null) {
                        response.append(line);
                    }
                    reader.close();

                    //pasrsing json untuk mendapatkan produk
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONArray productsArray = jsonResponse.getJSONArray("products");
                    int length = productsArray.length();
                    UASno3 [] products = new UASno3[length];

                    for (int i = 0; i < length; i++) {
                        JSONObject productObj = productsArray.getJSONObject(i);
                        int id = productObj.getInt("Id");
                        String title = productObj.getString("tittle");
                        double rating = productObj.getDouble("Rating");

                        UASno3 product = new UASno3(id, title, rating);
                        products[i] = product;
                    }
                    //menggunakan selection short untuk mengurutkan product berdasarkan rating
                    for (int i = 0; i < length - 1; i++) {
                        int minIndex = i;
                        for (int j = i + 1; j < length; j++) {
                            if (products[j].getRating() < products[minIndex].getRating()) {
                                minIndex = j;
                            }
                        }
                        UASno3 temp = products[minIndex];
                        products[minIndex] = products[i];
                        products[i] = temp;

                    }
                    // menampilkan product yang sudah diurutkan
                    System.out.println("Product berdasarkan rating dari yang terkecil ke yang terbesar");
                    for (UASno3 product : products) {
                        System.out.println("ID : " + product.getId() + ",Judul : " + product.gettitle() + ", Rating : " + product.getRating());
                    }
                } else {
                    System.out.println("Permintaan gagal.Kode respons : " +responseCode);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
