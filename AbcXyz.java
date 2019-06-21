/*
 * Which of the following methods is secure against SQL injection?
*/
public class AbcXyz {

    public List<AccountDTO> findAccountsByCustomerId1(String customerId) throws SQLException {
        String sql = "select "
                    + "customer_id,acc_number,branch_id,balance "
                    + "from Accounts where customer_id = '"
                    + customerId 
                    + "'";
        Connection c = dataSource.getConnection();
        ResultSet rs = c.createStatement().executeQuery(sql);
        // ...
    }

    public List<AccountDTO> findAccountsByCustomerId2(String customerId) {    
        String jql = "from Account where customerId = '" + customerId + "'";        
        TypedQuery<Account> q = em.createQuery(jql, Account.class);        
        return q.getResultList().stream().map(this::toAccountDTO).collect(Collectors.toList());        
    }
}