using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Data.SqlClient;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace FootballLeagues
{
    public partial class Form1 : Form
    {
        DataSet dataset = new DataSet();
        string connectionString = @"Server=DESKTOP-RPSMHMT\SQLEXPRESS;Database=NationalFootballLeagues;Integrated Security=true;";
        SqlDataAdapter parentAdapter = new SqlDataAdapter();
        SqlDataAdapter childAdapter = new SqlDataAdapter();
        BindingSource bsParent = new BindingSource();
        BindingSource bsChild = new BindingSource();

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    parentAdapter.SelectCommand = new SqlCommand("SELECT * FROM Teams;", connection);
                    childAdapter.SelectCommand = new SqlCommand("SELECT * FROM Players;", connection);
                    parentAdapter.Fill(dataset, "Teams");
                    childAdapter.Fill(dataset, "Players");
                    bsParent.DataSource = dataset.Tables["Teams"];
                    dataGridViewParent.DataSource = bsParent;
                    labelParent.Text = "Teams";
                    DataColumn pkColumn = dataset.Tables["Teams"].Columns["id_team"];
                    DataColumn fkColumn = dataset.Tables["Players"].Columns["id_team"];
                    DataRelation relation = new DataRelation("fk_Teams_Players", pkColumn, fkColumn);
                    dataset.Relations.Add(relation);
                    bsChild.DataSource = bsParent;
                    bsChild.DataMember = "fk_Teams_Players";
                    dataGridViewChild.DataSource = bsChild;
                    labelChild.Text = "Players";
                    //textBoxNume.DataBindings.Add("Text", bsParent, "nume", true);

                }
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void insertOperation()
        {
            try
            {
                using(SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand insertCommand = new SqlCommand("INSERT INTO Players (id_team, last_name_player, first_name_player, position, salary) " +
                        "VALUES(@idt, @lnp, @fnp, @pos, @sal);", connection);
                    int idt;
                    try
                    {
                        idt = (int)dataGridViewParent.CurrentRow.Cells["id_team"].Value;
                    }
                    catch(Exception ex)
                    {
                        MessageBox.Show("insertion error" + ex.Message);
                        return;
                    }
                    string lnp;
                    try
                    {
                        lnp = (string)dataGridViewChild.CurrentRow.Cells["last_name_player"].Value;
                    }
                    catch
                    {
                        lnp = "";
                    }
                    string fnp;
                    try
                    {
                        fnp = (string)dataGridViewChild.CurrentRow.Cells["first_name_player"].Value;
                    }
                    catch
                    {
                        fnp = "";
                    }
                    string pos;
                    try
                    {
                        pos = (string)dataGridViewChild.CurrentRow.Cells["position"].Value;
                    }
                    catch
                    {
                        pos = "";
                    }
                    decimal sal;
                    try {
                        sal = (decimal)dataGridViewChild.CurrentRow.Cells["salary"].Value;
                    }
                    catch
                    {
                        sal = 0;
                    }
                    insertCommand.Parameters.AddWithValue("idt", idt);
                    insertCommand.Parameters.AddWithValue("@lnp", lnp);
                    insertCommand.Parameters.AddWithValue("@fnp", fnp);
                    insertCommand.Parameters.AddWithValue("@pos", pos);
                    insertCommand.Parameters.AddWithValue("@sal", sal);
                    insertCommand.ExecuteNonQuery();

                    dataset.Tables["Players"].Clear();
                    childAdapter.SelectCommand = new SqlCommand("SELECT * FROM Players;", connection);
                    childAdapter.Fill(dataset, "Players");
                }

                MessageBox.Show("successfully insert");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void updateOperation()
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand updateCommand = new SqlCommand("UPDATE Players SET last_name_player=@lnp, first_name_player=@fnp, position=@pos, salary=@sal " +
                        "WHERE id_player = @idp;", connection);
                    int idp;
                    try
                    {
                        idp = (int)dataGridViewChild.CurrentRow.Cells["id_player"].Value;
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show("update error" + ex.Message);
                        return;
                    }
                    string lnp;
                    try
                    {
                        lnp = (string)dataGridViewChild.CurrentRow.Cells["last_name_player"].Value;
                    }
                    catch
                    {
                        lnp = "";
                    }
                    string fnp;
                    try
                    {
                        fnp = (string)dataGridViewChild.CurrentRow.Cells["first_name_player"].Value;
                    }
                    catch
                    {
                        fnp = "";
                    }
                    string pos;
                    try
                    {
                        pos = (string)dataGridViewChild.CurrentRow.Cells["position"].Value;
                    }
                    catch
                    {
                        pos = "";
                    }
                    decimal sal;
                    try
                    {
                        sal = (decimal)dataGridViewChild.CurrentRow.Cells["salary"].Value;
                    }
                    catch
                    {
                        sal = 0;
                    }
                    updateCommand.Parameters.AddWithValue("idp", idp);
                    updateCommand.Parameters.AddWithValue("@lnp", lnp);
                    updateCommand.Parameters.AddWithValue("@fnp", fnp);
                    updateCommand.Parameters.AddWithValue("@pos", pos);
                    updateCommand.Parameters.AddWithValue("@sal", sal);
                    updateCommand.ExecuteNonQuery();

                    dataset.Tables["Players"].Clear();
                    childAdapter.SelectCommand = new SqlCommand("SELECT * FROM Players;", connection);
                    childAdapter.Fill(dataset, "Players");
                }

                MessageBox.Show("successfully update");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void deleteOperation()
        {
            try
            {
                using (SqlConnection connection = new SqlConnection(connectionString))
                {
                    connection.Open();
                    SqlCommand deleteCommand = new SqlCommand("DELETE FROM Players WHERE id_player = @idp;", connection);
                    int idp;
                    try
                    {
                        idp = (int)dataGridViewChild.CurrentRow.Cells["id_player"].Value;
                    }
                    catch (Exception ex)
                    {
                        MessageBox.Show("update error" + ex.Message);
                        return;
                    }
                    
                    deleteCommand.Parameters.AddWithValue("idp", idp);
                    deleteCommand.ExecuteNonQuery();
                }

                MessageBox.Show("successfully delete");
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
        }

        private void dataGridViewChild_CellValueChanged(object sender, DataGridViewCellEventArgs e)
        {
            if(dataGridViewChild.CurrentRow != null)
            {
                DataGridViewRow dgvRow = dataGridViewChild.CurrentRow;
                if(dgvRow.Cells["id_player"].Value == DBNull.Value)
                {
                    insertOperation();
                    
                }
                else
                {
                    updateOperation();
                }
            }
        }

        private void dataGridViewChild_UserDeletingRow(object sender, DataGridViewRowCancelEventArgs e)
        {
            if (dataGridViewChild.CurrentRow != null)
            {
                DataGridViewRow dgvRow = dataGridViewChild.CurrentRow;
                if (dgvRow.Cells["id_player"].Value != DBNull.Value)
                {
                    if(MessageBox.Show("Are you sure to want delete this record?" , "Delete", MessageBoxButtons.YesNo) == DialogResult.Yes)
                    {
                        deleteOperation();
                    }
                }
            }
        }
    }
}
