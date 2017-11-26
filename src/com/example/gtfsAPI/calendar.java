package com.example.gtfsAPI;
import java.util.*;
import java.io.*;

public class calendar
{
    //stop_id,stop_code,stop_name,stop_lat,stop_lon
    public int service_id;
    public String date;

    public calendar (int id, String d)
    {
        this.service_id = id;
        this.date = d;
    }


}
