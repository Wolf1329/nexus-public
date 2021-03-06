/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2008-present Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Open Source Version is distributed with Sencha Ext JS pursuant to a FLOSS Exception agreed upon
 * between Sonatype, Inc. and Sencha Inc. Sencha Ext JS is licensed under GPL v3 and cannot be redistributed as part of a
 * closed source work.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
Ext.define('NX.coreui.controller.SearchP2', {
  extend: 'NX.app.Controller',
  requires: [
    'NX.I18n'
  ],

  /**
   * @override
   */
  init: function() {
    var me = this,
        search = me.getController('NX.coreui.controller.Search');

    search.registerCriteria([
      {
        id: 'attributes.p2.pluginName',
        group: NX.I18n.get('SearchP2_Group'),
        config: {
          format: 'p2',
          fieldLabel: NX.I18n.get('SearchP2_PluginName_FieldLabel'),
          width: 250
        }
      }
    ], me);

    search.registerFilter({
      id: 'p2',
      name: 'p2',
      text: NX.I18n.get('SearchP2_Text'),
      description: NX.I18n.get('SearchP2_Description'),
      readOnly: true,
      criterias: [
        {id: 'format', value: 'p2', hidden: true},
        {id: 'attributes.p2.pluginName'},
        {id: 'name.raw'}
      ]
    }, me);
  }
});
