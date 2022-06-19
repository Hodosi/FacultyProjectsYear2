using System;
using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using System.Linq;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace Deadlock
{

    class Program
    {
        public const int reincercari = 3;

        static Barrier barrier = new Barrier(participantCount: 2);
        static void Main(string[] args)
        {
     
            Thread thread1 = new Thread(delegate ()
            {
                callProcedure(1, "[dbo].[th1]");
            });
            Thread thread2 = new Thread(delegate ()
            {
                callProcedure(2, "[dbo].[th2]");
            });
            thread1.Start();
            thread2.Start();
            Console.ReadLine();
        }
        public static void callProcedure(int index, string procedureName)
        {
            bool executedSucces = false;
            Console.WriteLine($"Start thread {index}");
            string connectionString = @"Server=DESKTOP-RPSMHMT\SQLEXPRESS;Database=NationalFootballLeagues;Integrated Security=true;";
            using (SqlConnection conn = new SqlConnection(connectionString))
            {
                using (SqlCommand command = new SqlCommand($"{procedureName}", conn)
                {
                    CommandType = CommandType.StoredProcedure
                }
                )
                {
                    for (int i = 0; i < reincercari; i++)
                    {
                        Console.WriteLine($"Restart {procedureName} \n");
                        try
                        {
                            conn.Open();
                            barrier.SignalAndWait();
                            command.ExecuteNonQuery();
                            executedSucces = true;
                            break;
                        }
                        catch (Exception)
                        {
                            Console.WriteLine($"Deadlock: {procedureName} \n");
                        }
                    }
                    if (executedSucces)
                        Console.WriteLine($"Finish: {procedureName} \n");
                    else
                        Console.WriteLine($"Abort: {procedureName} \n");
                }
            }
        }

    }
}
