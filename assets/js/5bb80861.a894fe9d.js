"use strict";(self.webpackChunk=self.webpackChunk||[]).push([[124],{3905:(e,t,r)=>{r.d(t,{Zo:()=>d,kt:()=>m});var o=r(7294);function a(e,t,r){return t in e?Object.defineProperty(e,t,{value:r,enumerable:!0,configurable:!0,writable:!0}):e[t]=r,e}function n(e,t){var r=Object.keys(e);if(Object.getOwnPropertySymbols){var o=Object.getOwnPropertySymbols(e);t&&(o=o.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),r.push.apply(r,o)}return r}function l(e){for(var t=1;t<arguments.length;t++){var r=null!=arguments[t]?arguments[t]:{};t%2?n(Object(r),!0).forEach((function(t){a(e,t,r[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(r)):n(Object(r)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(r,t))}))}return e}function i(e,t){if(null==e)return{};var r,o,a=function(e,t){if(null==e)return{};var r,o,a={},n=Object.keys(e);for(o=0;o<n.length;o++)r=n[o],t.indexOf(r)>=0||(a[r]=e[r]);return a}(e,t);if(Object.getOwnPropertySymbols){var n=Object.getOwnPropertySymbols(e);for(o=0;o<n.length;o++)r=n[o],t.indexOf(r)>=0||Object.prototype.propertyIsEnumerable.call(e,r)&&(a[r]=e[r])}return a}var c=o.createContext({}),s=function(e){var t=o.useContext(c),r=t;return e&&(r="function"==typeof e?e(t):l(l({},t),e)),r},d=function(e){var t=s(e.components);return o.createElement(c.Provider,{value:t},e.children)},u="mdxType",p={inlineCode:"code",wrapper:function(e){var t=e.children;return o.createElement(o.Fragment,{},t)}},b=o.forwardRef((function(e,t){var r=e.components,a=e.mdxType,n=e.originalType,c=e.parentName,d=i(e,["components","mdxType","originalType","parentName"]),u=s(r),b=a,m=u["".concat(c,".").concat(b)]||u[b]||p[b]||n;return r?o.createElement(m,l(l({ref:t},d),{},{components:r})):o.createElement(m,l({ref:t},d))}));function m(e,t){var r=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var n=r.length,l=new Array(n);l[0]=b;var i={};for(var c in t)hasOwnProperty.call(t,c)&&(i[c]=t[c]);i.originalType=e,i[u]="string"==typeof e?e:a,l[1]=i;for(var s=2;s<n;s++)l[s]=r[s];return o.createElement.apply(null,l)}return o.createElement.apply(null,r)}b.displayName="MDXCreateElement"},9731:(e,t,r)=>{r.r(t),r.d(t,{assets:()=>c,contentTitle:()=>l,default:()=>p,frontMatter:()=>n,metadata:()=>i,toc:()=>s});var o=r(7462),a=(r(7294),r(3905));const n={},l="Adding extra collaborators",i={unversionedId:"adding-extra-collaborators",id:"adding-extra-collaborators",title:"Adding extra collaborators",description:"The collaborators and developers settings are populated with the information extracted from the repository collaborator list (who are also contributors). If you want to include extra collaborators, you can use the extraCollaborators setting:",source:"@site/target/mdoc/adding-extra-collaborators.md",sourceDirName:".",slug:"/adding-extra-collaborators",permalink:"/sbt-github/docs/adding-extra-collaborators",draft:!1,editUrl:"https://github.com/alejandrohdezma/sbt-github/edit/main/site/docs/adding-extra-collaborators.md",tags:[],version:"current",frontMatter:{},sidebar:"defaultSidebar",previous:{title:"Getting Started",permalink:"/sbt-github/docs/getting-started"},next:{title:"Excluding contributors",permalink:"/sbt-github/docs/excluding-contributors"}},c={},s=[],d={toc:s},u="wrapper";function p(e){let{components:t,...r}=e;return(0,a.kt)(u,(0,o.Z)({},d,r,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h1",{id:"adding-extra-collaborators"},"Adding extra collaborators"),(0,a.kt)("p",null,"The ",(0,a.kt)("inlineCode",{parentName:"p"},"collaborators")," and ",(0,a.kt)("inlineCode",{parentName:"p"},"developers")," settings are populated with the information extracted from the repository collaborator list (who are also contributors). If you want to include extra collaborators, you can use the ",(0,a.kt)("inlineCode",{parentName:"p"},"extraCollaborators")," setting: "),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-scala"},'ThisBuild / extraCollaborators += Collaborator(\n    "alejandrohdezma",\n    "Alejandro Hern\xe1ndez",\n    "https://github.com/alejandrohdezma"\n)\n')),(0,a.kt)("p",null,"If you don't want to provide all the details for an extra collaborator, you can use the ",(0,a.kt)("inlineCode",{parentName:"p"},"Collaborator.github")," constructor and let ",(0,a.kt)("inlineCode",{parentName:"p"},"sbt-github")," download its information for you:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-scala"},'ThisBuild / extraCollaborators += Collaborator.github("alejandrohdezma")\n')))}p.isMDXComponent=!0}}]);