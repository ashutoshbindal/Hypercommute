package com.example.gtfsAPI;
import java.util.*;
import java.io.*;

public class graphStops
{
    //stop_id,stop_code,stop_name,stop_lat,stop_lon
    public int stop_id;
    public String trip_id;
    public long dept_time;
    public long travel_time;

    public graphStops (int id, String name, long d, long t)
    {
        this.stop_id = id;
        this.trip_id = name;
        this.dept_time = d;
        this.travel_time = t;
    }


}
