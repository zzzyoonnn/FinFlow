package com.FinFlow.repository;

import com.FinFlow.domain.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;

interface DAO {
  List<Transaction> findTransactionList(@Param("accountId") Long accountId, @Param("transaction_type") String transaction_type, @Param("page") Integer page);
}

@RequiredArgsConstructor
// Impl 꼭 붙이고 TransactionRepository가 앞에 붙어야 한다.
public class TransactionRepositoryImpl implements DAO {

  private final EntityManager em;

  @Override
  public List<Transaction> findTransactionList(Long accountId, String transaction_type, Integer page) {
    // 동적 쿼리 (transaction_type 값을 가지고 동적 쿼리 = DEPOSIT, WITHDRAW, ALL)

    // JPQL
    String sql = "";
    sql += "select t from Transaction t ";

    if (transaction_type.equals("WITHDRAW")) {
      sql += "join fetch t.withdrawAccount wa ";
      sql += "where t.withdrawAccount.id = :withdrawAccountId ";
    } else if (transaction_type.equals("DEPOSIT")) {
      sql += "join fetch t.depositAccount da ";
      sql += "where t.depositAccount.id = :depositAccountId ";
    } else {
      sql += "left join fetch t.withdrawAccount wa ";
      sql += "left join fetch t.depositAccount da ";
      sql += "where t.withdrawAccount.id = :withdrawAccountId ";
      sql += "or ";
      sql += "t.depositAccount.id = :depositAccountId ";
    }

    TypedQuery<Transaction> query = em.createQuery(sql, Transaction.class);

    if (transaction_type.equals("WITHDRAW")) {
      query = query.setParameter("withdrawAccountId", accountId);
    } else if (transaction_type.equals("DEPOSIT")) {
      query = query.setParameter("depositAccountId", accountId);
    } else {
      query = query.setParameter("withdrawAccountId", accountId);
      query = query.setParameter("depositAccountId", accountId);
    }

    query.setFirstResult(page * 5);
    query.setMaxResults(5);

    return query.getResultList();
  }
}
