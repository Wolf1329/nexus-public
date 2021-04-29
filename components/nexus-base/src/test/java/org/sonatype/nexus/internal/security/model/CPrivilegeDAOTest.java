/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.internal.security.model;

import org.sonatype.goodies.testsupport.TestSupport;
import org.sonatype.nexus.content.testsuite.groups.SQLTestGroup;
import org.sonatype.nexus.datastore.api.DataSession;
import org.sonatype.nexus.datastore.api.DataStoreManager;
import org.sonatype.nexus.testdb.DataSessionRule;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Iterables;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static java.util.Collections.emptyMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

@Category(SQLTestGroup.class)
public class CPrivilegeDAOTest
    extends TestSupport
{
  @Rule
  public DataSessionRule sessionRule = new DataSessionRule().access(CPrivilegeDAO.class);

  private DataSession session;

  private CPrivilegeDAO dao;

  @Before
  public void setup() {
    session = sessionRule.openSession(DataStoreManager.DEFAULT_DATASTORE_NAME);
    dao = (CPrivilegeDAO) session.access(CPrivilegeDAO.class);
  }

  @After
  public void cleanup() {
    session.close();
  }

  @Test
  public void testCreateReadUpdateDelete() {
    CPrivilegeData privilege = new CPrivilegeData();
    privilege.setId("notes-read");
    privilege.setName("app-notes-read");
    privilege.setDescription("Notes Read");
    privilege.setType("application");
    privilege.setProperties(ImmutableMap.of("domain", "notes", "actions", "view"));
    privilege.setReadOnly(true);
    dao.create(privilege);

    CPrivilegeData read = dao.read("notes-read").get();

    assertThat(read, not(nullValue()));
    assertThat(read.getId(), is("notes-read"));
    assertThat(read.getName(), is("app-notes-read"));
    assertThat(read.getDescription(), is("Notes Read"));
    assertThat(read.getType(), is("application"));
    assertThat(read.getProperties(), is(ImmutableMap.of("domain", "notes", "actions", "view")));
    assertThat(read.isReadOnly(), is(true));

    privilege.setName("app-notes-read2");
    privilege.setDescription("Notes Read2");
    privilege.setType("application2");
    privilege.setProperties(ImmutableMap.of("domain", "apps", "actions", "read,view"));
    privilege.setReadOnly(false);
    dao.update(privilege);

    read = dao.read("notes-read").get();

    assertThat(read, not(nullValue()));
    assertThat(read.getName(), is("app-notes-read2"));
    assertThat(read.getDescription(), is("Notes Read2"));
    assertThat(read.getType(), is("application2"));
    assertThat(read.getProperties(), is(ImmutableMap.of("domain", "apps", "actions", "read,view")));
    assertThat(read.isReadOnly(), is(false));

    dao.delete("notes-read");

    assertThat(dao.read("notes-read").isPresent(), is(false));
  }

  @Test
  public void testBrowse() {
    CPrivilegeData privilege1 = new CPrivilegeData();
    privilege1.setId("privilege1");
    privilege1.setName("Privilege1");
    privilege1.setDescription("Privilege 1");
    privilege1.setType("Application");
    privilege1.setProperties(emptyMap());
    privilege1.setReadOnly(false);
    CPrivilegeData privilege2 = new CPrivilegeData();
    privilege2.setId("privilege2");
    privilege2.setName("Privilege2");
    privilege2.setDescription("Privilege 2");
    privilege2.setType("Application");
    privilege2.setProperties(emptyMap());
    privilege2.setReadOnly(false);
    CPrivilegeData privilege3 = new CPrivilegeData();
    privilege3.setId("privilege3");
    privilege3.setName("Privilege3");
    privilege3.setDescription("Privilege 3");
    privilege3.setType("Application");
    privilege3.setProperties(emptyMap());
    privilege3.setReadOnly(false);

    dao.create(privilege1);
    dao.create(privilege2);
    dao.create(privilege3);

    assertThat(Iterables.size(dao.browse()), is(3));
  }

  @Test
  public void testUpdate_returnsStatus() {
    CPrivilegeData privilege = new CPrivilegeData();
    privilege.setId("privilege1");
    privilege.setName("Privilege1");
    privilege.setDescription("Privilege 1");
    privilege.setType("Application");
    privilege.setProperties(emptyMap());
    privilege.setReadOnly(false);

    assertThat(dao.update(privilege), is(false));

    dao.create(privilege);
    privilege.setDescription("Privilege 2");

    assertThat(dao.update(privilege), is(true));
  }
}
