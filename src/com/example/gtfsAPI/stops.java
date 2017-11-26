package com.example.gtfsAPI;
import java.util.*;
import java.io.*;

public class stops
{
    //stop_id,stop_code,stop_name,stop_lat,stop_lon
    public int stop_id;
    public long stop_code;
    public String stop_name;
    public float stop_lat;
    public float stop_lon;

    public stops (int id, long code, String name, float lat, float lon)
    {
        this.stop_id = id;
        this.stop_name = name;
        this.stop_code = code;
        this.stop_lat = lat;
        this.stop_lon = lon;
    }


}
