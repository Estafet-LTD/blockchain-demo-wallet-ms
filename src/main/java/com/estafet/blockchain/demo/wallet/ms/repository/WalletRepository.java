package com.estafet.blockchain.demo.wallet.ms.repository;

import com.estafet.blockchain.demo.wallet.ms.model.Wallet;
import org.springframework.data.couchbase.core.query.N1qlPrimaryIndexed;
import org.springframework.data.couchbase.core.query.ViewIndexed;
import org.springframework.data.couchbase.repository.CouchbasePagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@ViewIndexed(designDoc = "wallet")
@N1qlPrimaryIndexed
public interface WalletRepository extends CouchbasePagingAndSortingRepository<Wallet, String> {

    List<Wallet> findAll();
}
