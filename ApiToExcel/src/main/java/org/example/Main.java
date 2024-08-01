package org.example;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Main {

    public static String hitApi() throws IOException, InterruptedException{
        String url = "https://api.github.com/users/AquibMS";

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept","application/json")
                .build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200){
            throw new IOException("HTTP error code: "+ response.statusCode());
        }

        return response.body();
    }

    public static void JsonToExcel(String jsonResponse) throws IOException{
        JSONObject jsonObject = new JSONObject(jsonResponse);

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("API Data");

        // Create Header Row
        Row headerRow = sheet.createRow(0);
        int cellIndex = 0;
        for(String key : jsonObject.keySet()){
            Cell cell = headerRow.createCell(cellIndex++);
            cell.setCellValue(key);
        }

        // Populate Data Rows
        Row row = sheet.createRow(1);
        cellIndex = 0;
        for(String key : jsonObject.keySet()){
            Cell cell = row.createCell(cellIndex++);
            cell.setCellValue(jsonObject.get(key).toString());
        }


        // writing to Excel file
        try(FileOutputStream fileOutputStream = new FileOutputStream("ApiData.xlsx")){
            workbook.write(fileOutputStream);
        }

        workbook.close();
    }

    public static void main(String[] args) {
        try{
            String response = hitApi();
            JsonToExcel(response);
            System.out.println("Data written to excel successfully");
        } catch (IOException | InterruptedException e){
            System.out.println("Data failed to fetch from API");
        }
    }
}