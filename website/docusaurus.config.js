import {themes as prismThemes} from 'prism-react-renderer';

/** @type {import('@docusaurus/types').Config} */
const config = {
  title: 'sbt-github',
  tagline: 'SBT plugin to read several settings from Github',
  favicon: 'img/favicon.ico',

  url: 'https://alejandrohdezma.github.io',
  baseUrl: '/sbt-github/',
  trailingSlash: true,

  organizationName: 'alejandrohdezma',
  projectName: 'sbt-github',

  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',

  i18n: {
    defaultLocale: 'en',
    locales: ['en'],
  },

  presets: [
    [
      'classic',
      /** @type {import('@docusaurus/preset-classic').Options} */
      ({
        docs: {
          path: 'target/mdoc/',
          editUrl: params => 'https://github.com/alejandrohdezma/sbt-github/edit/main/website/docs/' + params.docPath,
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      }),
    ],
  ],

  themeConfig:
    /** @type {import('@docusaurus/preset-classic').ThemeConfig} */
    ({
      image: 'img/removed_settings.png',
      colorMode: {
        disableSwitch: true,
        respectPrefersColorScheme: true,
      },
      algolia: {
        appId: 'HI501929UG',
        apiKey: 'dcf994619a62ba649248078eb56a6ee6',
        indexName: 'sbt'
      },
      navbar: {
        title: 'sbt-github',
        logo: {
          alt: 'sbt-github Logo',
          src: 'img/logo.svg',
        },
        items: [
          {
            type: 'doc',
            docId: 'getting-started',
            position: 'left',
            label: 'Docs',
          },
          {
            href: 'https://github.com/alejandrohdezma/sbt-github',
            className: "header-github-link",
            "aria-label": "GitHub repository",
            position: 'right',
          },
        ],
      },
      prism: {
        theme: prismThemes.github,
        darkTheme: prismThemes.dracula,
        additionalLanguages: ['java', 'scala'],
      },
    }),
};

module.exports = config;
