package com.example.gtfsAPI;
import java.util.*;
import java.io.*;
// import stops.*;

public class Solution
{
    public static ArrayList<stops> node = new ArrayList<>();
    public static ArrayList<graphStops>[] graph = new ArrayList[606];

    // Solution()
    // {
    // 	for (int i = 0; i < 606; i++)
    // 	{
    // 	    graph[i] = new ArrayList<>();
    // 	}
    // }

    //function to get the time in route
    public static long getTime(ArrayList<graphStops> a)
    {
        long t = 0;
        t = a.get(a.size()-1).dept_time + a.get(a.size()-1).travel_time;
        // for(int i=0; i<a.size(); i++)
        // {
        // 	t += a.get(i).dept_time;
        // }
        return t;
    }

    public static ArrayList<graphStops> findRoute(int s, int d, ArrayList<Integer> v, int h, String t_id, long d_t)
    {
        if(h <= 0)
            return null;
        // int index = 0;
        ArrayList<graphStops> ans = new ArrayList<>();
        int i=0;
        for(; i<node.size(); i++)
        {
            if(node.get(i).stop_id == s)
                break;
        }
        ArrayList<graphStops> toCheck = new ArrayList<>();
        toCheck = graph[i];
        boolean flag = false;
        int k=0;
        for(; k<toCheck.size(); k++)
        {
            if(toCheck.get(k).stop_id == d)
            {
                flag = true;
                break;
            }
        }
        if(flag)
        {
            ans.add(toCheck.get(k));
            return ans;
        }
        else
        {
            // int flag = 0;
            for(int j=0; j<graph[i].size(); j++)
            {
                // ArrayList<graphStops> toCheck1 = new ArrayList<>();
                // toCheck1 = graph[i];

                //checking validity with time
                if(graph[i].get(j).dept_time < d_t)
                {
                    continue;
                }

                boolean flag1 = false;
                for(int l=0; l<v.size(); l++)
                {
                    if(graph[i].get(j).stop_id == v.get(l))
                    {
                        flag1 = true;
                        break;
                    }
                }
                if(!flag1)
                {
                    if(t_id == null)
                    {
                        t_id = graph[i].get(j).trip_id;
                    }
                    else if(!graph[i].get(j).trip_id.equals(t_id))
                    {
                        h = h-1;
                        t_id = graph[i].get(j).trip_id;
                    }
                    ArrayList<graphStops> temp = new ArrayList<>();
                    ArrayList<Integer> test_temp = new ArrayList<>();
                    test_temp = v;
                    test_temp.add(graph[i].get(j).stop_id);
                    temp = findRoute(graph[i].get(j).stop_id, d, test_temp, h, t_id, graph[i].get(j).dept_time + graph[i].get(j).travel_time);
                    if(temp != null)
                    {
                        if(ans.size() == 0 || ans == null)
                        {
                            ans.add(graph[i].get(j));
                            for(int z=0; z<temp.size(); z++)
                                ans.add(temp.get(z));
                        }
                        else
                        {
                            if(getTime(ans) > getTime(temp))
                            // if(ans.size() > (temp.size() + 1))
                            {
                                ans.clear();
                                ans.add(graph[i].get(j));
                                for(int z=0; z<temp.size(); z++)
                                    ans.add(temp.get(z));
                            }
                        }
                    }
                    else
                    {
                        continue;
                    }
                }
            }
            if(ans.size() > 0)
                return ans;
            else
                return null;
        }
        // return ans;
    }

    public static void main (String [] args) throws IOException
    {

        //Nodes
        InputStream is = Solution.class.getResourceAsStream("/stops.txt");
        BufferedReader br= new BufferedReader(new InputStreamReader(is));
//        BufferedReader br = new BufferedReader(new FileReader("/stops.txt"));

        String line = "";
        // ArrayList<stops> node = new ArrayList<>();

        line = br.readLine();
        int c = 0;
        while((line = br.readLine()) != null)
        {
            String[] temp = line.split(",");
            stops s = new stops(Integer.parseInt(temp[0]), Long.parseLong(temp[1]), temp[2], Float.parseFloat(temp[3]), Float.parseFloat(temp[4]));
            node.add(s);
            c++;
        }
        br.close();

        //Calendar dates
        InputStream is2 = Solution.class.getResourceAsStream("/calendar_dates.txt");
        BufferedReader br2= new BufferedReader(new InputStreamReader(is2));
//        BufferedReader br2 = new BufferedReader(new FileReader("/calendar_dates.txt"));

        String line2 = "";
        line2 = br2.readLine();
        ArrayList<calendar> cal = new ArrayList<>();

        while((line2 = br2.readLine()) != null)
        {
            String[] temp = line2.split(",");
            calendar ca = new calendar(Integer.parseInt(temp[0]), temp[1]);
            cal.add(ca);
        }
        br2.close();

        //inputs
        Scanner scan = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the source stop id: ");
        int s = scan.nextInt();
        System.out.println("Enter the destination stop id: ");
        int d = scan.nextInt();
        System.out.println("Enter the number of hops: ");
        int hops = scan.nextInt();
        System.out.println("Enter the time of departure: ");
        String time = scanner.nextLine();

        String[] ti = time.split(":");
        long depart_time = (Long.parseLong(ti[0])*3600) + (Long.parseLong(ti[1])*60) + Long.parseLong(ti[2]);

        //finding the sevice id
        boolean serv = false;
        int service_id = 0;
        while(!serv)
        {
            System.out.println("Enter the date of travel: ");
            String travel_date = scanner.nextLine();
            for(int i=0; i<cal.size(); i++)
            {
                if(cal.get(i).date.equals(travel_date))
                {
                    serv = true;
                    service_id = cal.get(i).service_id;
                    break;
                }
            }
            if(!serv)
            {
                System.out.println("There is no service available on this day. Try some other time!");
            }
        }

        //list of all valid trip id's
        InputStream is3 = Solution.class.getResourceAsStream("/trips.txt");
        BufferedReader br3= new BufferedReader(new InputStreamReader(is3));
//        BufferedReader br3 = new BufferedReader(new FileReader("/trips.txt"));
        ArrayList<String> valid_trip = new ArrayList<>();
        String line3 = "";
        line3 = br3.readLine();
        while((line3 = br3.readLine()) != null)
        {
            String[] temp = line3.split(",");
            if(Integer.parseInt(temp[1]) == service_id)
            {
                valid_trip.add(temp[2]);
            }
        }

        br3.close();

        //Edges
        InputStream is1 = Solution.class.getResourceAsStream("/stop_times.txt");
        BufferedReader br1= new BufferedReader(new InputStreamReader(is1));
//        BufferedReader br1 = new BufferedReader(new FileReader("/stop_times.txt"));

        // BufferedWriter bw = new BufferedWriter(new FileWriter("temp.txt"));
        // int flag = 0;
        // String test = "";

        // ArrayList<Integer>[] graph = new ArrayList<>()[c];
        // ArrayList<Integer>[] graph = new ArrayList[c];
        for (int i = 0; i < c; i++) {
            graph[i] = new ArrayList<>();
        }

        String line1 = "";
        line1 = br1.readLine();
        String id = "";
        int stopId = -1;
        long d_time = 0;
        while((line1 = br1.readLine()) != null)
        {

            String[] temp = line1.split(",");
            if(!valid_trip.contains(temp[0]))
            {
                continue;
            }
            //testing
            // if(flag == 1 && test.equals(temp[0]))
            // {
            // 	flag = 0;
            // 	bw.write(line1 + "\n");
            // }
            // else if(Integer.parseInt(temp[1]) == 1)
            // {
            // 	flag = 1;
            // 	test = temp[0];
            // }

            if(id == null)
            {
                id = temp[0];
                stopId = Integer.parseInt(temp[1]);
                if(temp[3] != null)
                {
                    String[] t = temp[3].split(":");
                    d_time = (Long.parseLong(t[0])*3600) + (Long.parseLong(t[1])*60) + Long.parseLong(t[2]);
                }
            }
            else if(id.equals(temp[0]))
            {
                int i=0;
                for(; i<node.size(); i++)
                {
                    if(node.get(i).stop_id == stopId)
                        break;
                }
                // if(graph[i] != null)
                // {
                ArrayList<graphStops> toCheck = new ArrayList<>();
                toCheck = graph[i];
                boolean flag = false;
                for(int y=0; y<toCheck.size(); y++)
                {
                    if(toCheck.get(y).stop_id == Integer.parseInt(temp[1]) && toCheck.get(y).dept_time == d_time)
                    {
                        flag = true;
                        break;
                    }
                }
                if(!flag)
                {
                    graphStops t = new graphStops(Integer.parseInt(temp[1]), id, d_time, Long.parseLong(temp[4]));
                    graph[i].add(t);
                    // d_time = d_time + Long.parseLong(temp[4]);
                }
                // }
                // else
                // {
                // 	graph[i].add(Integer.parseInt(temp[1]));
                // }
                stopId = Integer.parseInt(temp[1]);
            }
            else
            {
                id = temp[0];
                stopId = Integer.parseInt(temp[1]);
                if(temp[3] != null)
                {
                    String[] t = temp[3].split(":");
                    d_time = (Long.parseLong(t[0])*3600) + (Long.parseLong(t[1])*60) + Long.parseLong(t[2]);
                }
            }
        }

        br1.close();
        // bw.close();

        //Test
        // System.out.println(node.get(0).stop_id);
        // for(int i=0; i<graph[0].size(); i++)
        // 	System.out.println(graph[0].get(i));

        //finding the route
        ArrayList<Integer> route = new ArrayList<>();
        route.add(s);

        ArrayList<Integer> visited = new ArrayList<>();
        visited.add(s);

        ArrayList<graphStops> r = new ArrayList<>();
        String tr_id = null;
        r = findRoute(s, d, visited, hops, tr_id, depart_time);
        if(r!=null)
        {
            for(int j=0; j<r.size(); j++)
            {
                route.add(r.get(j).stop_id);
            }

            for(int k = 0; k<route.size(); k++)
                System.out.println(route.get(k));

            //testing
            // long test_time = getTime(r);
            // long x = 3600;
            // long v = 60;
            // long h = test_time/x;
            // long m = (test_time - (x*h))/v;
            // long se = test_time - (h*x) - (m*v);
            // System.out.println(h + ":" + m + ":" + se);
        }
        else
        {
            System.out.println("Sorry, not route found between " + s + " and " + d);
        }

    }
}
