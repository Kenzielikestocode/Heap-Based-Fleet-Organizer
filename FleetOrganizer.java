import java.util.*;
import java.io.FileReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.Scanner;
import java.io.*;
import java.util.*;

/**
 * A testbed for the Heap implementation; sorts car data in various orders
 *
 * @author Mackenzie Millican
 * <pre>
 * File: FleetOrganizer.cpp
 * Date: 2/19/2026
 * Course: csc 3102.001
 * Programming Project # 1
 * Instructor: Dr. Duncan
 *
 * Usage: FleetOrganizer <data-file> <order-key>
 * Note: Record for the vehicles must be organized in the data text file with
 *       records, one per row, and with fields comma-delimited, as follows:
 *       <year>, <make>, <model>, <type>
 *
 * DO NOT REMOVE THIS NOTICE (GNU GPL V2):
 * Contact Information: duncanw@lsu.edu
 * Copyright (c) 2026 William E. Duncan
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>
 * </pre>
 * @see HeapAPI.//java, CarAPI.java
 */

public class FleetOrganizer {
    /**
     * //@param args[0] <data-file>
     * //@param args[1] <order-key>
     */
    public static void main(String[] args) throws IOException, PQueueException {
        String usage = "Usage: FleetOrganizer <data-file> <order-key>\n";
        usage += "<data-file>:\n";
        usage += "Record for the vehicles must be organized in the data file with records, one per row,\n";
        usage += "and with fields comma-delimited, as follows:\n";
        usage += "<year>,<make>,<model>,<type>\n";
        usage += "<order-key>:\n";
        usage += " 2 -> +make+model+type+year\n";
        usage += " 1 -> +year+make+model+type\n";
        usage += " 0 -> -type+year-make-model\n";
        usage += "-1 -> -year-make-model-type\n";
        usage += "-2 -> -make-model-type-year\n";

        // test case if the command input is incorrect, ie. more than 1 argument
        //@return the usage info and terminates
        if (args.length != 2) {
            System.out.print(usage);
            return;
        }

        String filename = args[0];
        int order_key;

        // decides the input order-key
        try {
            order_key = Integer.parseInt(args[1]);
        } catch (NumberFormatException a) {
            System.out.print(usage);
            return;
        }

        // Verifies order-code is between -2 and 2
        if (order_key < -2 || order_key > 2) {
            System.out.println(usage);
            return;
        }


        Comparator<Car> comparator = getComparator(order_key);

        PQueue<Car> pq = new PQueue<>(comparator);

        // Verifies that data file is successfully opened and read records
        try {
            Scanner scanner = new Scanner(new File(args[0]));

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] parts = line.split(",");

            int year = Integer.parseInt(parts[0]);
            String make = parts[1].toUpperCase();
            String model = parts[2].toUpperCase();
            String type = parts[3].toUpperCase();

            Car car = new Car(year, make, model, type);
            pq.add(car);

        }
        scanner.close();

        } catch (IOException e) {
        System.out.print(usage);
        return;}

        //prints the description for the order-key
        String getOrderKeyDescription = "";

        if (order_key == -2) {
            getOrderKeyDescription= "-make-model-type-year";
        } else if (order_key == -1) {
            getOrderKeyDescription = "-year-make-mode-type";
        } else if (order_key == 0) {
            getOrderKeyDescription = "-type+year-make-model";
        } else if (order_key == 1) {
            getOrderKeyDescription = "+year+make+model+type";
        } else if (order_key == 2) {
            getOrderKeyDescription = "+make+model+type+year";
        } else {
            System.out.print(usage);}

        System.out.println("Fleet: " + getOrderKeyDescription);


        // Perform deletions to empty the heap
        while (!pq.isEmpty()) {
            Car car = pq.remove();
            System.out.println(car);
            System.out.println(pq.remove());
        }
    }

    /**
     * Returns the appropriate comparator based on the order-code
     * @param orderKey the sorting order code
     * @return a Comparator for Car objects
     */
    private static Comparator<Car> getComparator(int orderKey) {
        switch (orderKey) {
            case 2:  // +make+model+type+year
                return (c1, c2) -> {
                    int result = c1.getMake().compareTo(c2.getMake());
                    if (result != 0) return result;
                    result = c1.getModel().compareTo(c2.getModel());
                    if (result != 0) return result;
                    result = c1.getType().compareTo(c2.getType());
                    if (result != 0) return result;
                    return c1.getYear() - c2.getYear();
                };

            case 1:  // +year+make+model+type (default compareTo)
                return Car::compareTo;

            case 0:  // -type+year-make-model
                return (c1, c2) -> {
                    int result = c2.getType().compareTo(c1.getType());  // descending
                    if (result != 0) return result;
                    result = c1.getYear() - c2.getYear();  // ascending
                    if (result != 0) return result;
                    result = c2.getMake().compareTo(c1.getMake());  // descending
                    if (result != 0) return result;
                    return c2.getModel().compareTo(c1.getModel());  // descending
                };

            case -1:  // -year-make-model-type
                return (c1, c2) -> {
                    int result = c2.getYear() - c1.getYear();  // descending
                    if (result != 0) return result;
                    result = c2.getMake().compareTo(c1.getMake());  // descending
                    if (result != 0) return result;
                    result = c2.getModel().compareTo(c1.getModel());  // descending
                    if (result != 0) return result;
                    return c2.getType().compareTo(c1.getType());  // descending
                };

            case -2:  // -make-model-type-year
                return (c1, c2) -> {
                    int result = c2.getMake().compareTo(c1.getMake());  // descending
                    if (result != 0) return result;
                    result = c2.getModel().compareTo(c1.getModel());  // descending
                    if (result != 0) return result;
                    result = c2.getType().compareTo(c1.getType());  // descending
                    if (result != 0) return result;
                    return c2.getYear() - c1.getYear();  // descending
                };

            default:
                return (c1, c2) -> c1.compareTo(c2);
        }
    }
}

