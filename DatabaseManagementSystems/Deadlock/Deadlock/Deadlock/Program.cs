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
        /*
        static void Main(string[] args)
        {
        }
        */

        private static SqlConnection connection;
        static void Main(string[] args)
        {
            connection = new SqlConnection(@"Server=DESKTOP-RPSMHMT\SQLEXPRESS;Database=NationalFootballLeagues;Integrated Security=true;");
            connection.Open();

            Thread thread1 = new Thread(runThread1);
            Thread thread2 = new Thread(runThread2);

            thread1.Start();
            thread2.Start();

            var wait = Console.ReadLine();
        }

        private static void runThread1()
        {
            Console.WriteLine("Start thread1");
            try
            {
                SqlCommand command = new SqlCommand("th1", connection);
                command.CommandType = CommandType.StoredProcedure;
                command.ExecuteNonQuery();
                Console.WriteLine("Finish thread1");
            }
            catch (SqlException ex)
            {
                if (ex.Number == 1205)
                {
                    Console.WriteLine("Deadlock in thread1");
                }
                else
                {
                    Console.WriteLine(ex.Message);
                }
            }
        }

        private static void runThread2()
        {
            try
            {
                Console.WriteLine("Start thread2");
                SqlCommand command = new SqlCommand("th2", connection);
                command.CommandType = CommandType.StoredProcedure;
                command.ExecuteNonQuery();
                Console.WriteLine("Finish thread2");
            }
            catch (SqlException ex)
            {
                if (ex.Number == 1205)
                {
                    Console.WriteLine("Deadlock in thread2");
                }
                else
                {
                    Console.WriteLine(ex.Message);
                }
            }
        }
    }
}
