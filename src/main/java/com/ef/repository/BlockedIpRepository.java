package com.ef.repository;

import com.ef.domain.BlockedIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by guilherme-lima on 15/07/18.
 * https://github.com/guilherme-lima
 */
@Repository
public interface BlockedIpRepository extends JpaRepository<BlockedIp, String> {}